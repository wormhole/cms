package net.stackoverflow.cms.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.vo.UserVO;
import net.stackoverflow.cms.security.CmsMd5PasswordEncoder;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.JsonUtils;
import net.stackoverflow.cms.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 用户注册Controller
 *
 * @author 凉衫薄
 */
@RestController
@Slf4j
public class RegisterController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private CmsMd5PasswordEncoder encoder;

    /**
     * 用户注册接口
     *
     * @param userVO
     * @param session
     * @return
     */
    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody UserVO userVO, HttpSession session) throws JsonProcessingException {

        log.info("请求参数：" + JsonUtils.bean2json(userVO));

        Result result = new Result();

        //校验验证码
        String vcode = (String) session.getAttribute("vcode");
        if (!vcode.equalsIgnoreCase(userVO.getVcode())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("验证码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        //校验数据
        if (StringUtils.isBlank(userVO.getUsername())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("用户名不能为空");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            User user = userService.findByUsername(userVO.getUsername());
            if (user != null) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("用户名重复");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        }
        if (!validateTelephone(userVO.getTelephone())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("电话号码格式错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        if (!validateEmail(userVO.getEmail())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("邮箱格式错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        if (!validatePassword(userVO.getPassword())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("密码长度不能小于6");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        //保存至数据库
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        user.setId(UUID.randomUUID().toString());
        user.setEnabled(1);
        user.setDeletable(1);
        user.setPassword(encoder.encode(user.getPassword()));
        userService.saveWithRole(user, userVO.getRole());

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("注册成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
