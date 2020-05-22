package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.vo.UserVO;
import net.stackoverflow.cms.security.CmsMd5PasswordEncoder;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api")
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
    public ResponseEntity register(@RequestBody @Validated(UserVO.Insert.class) UserVO userVO, HttpSession session) {
        Result result = new Result();

        //校验验证码
        String code = (String) session.getAttribute("code");
        if (!code.equalsIgnoreCase(userVO.getCode())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("验证码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        //校验数据
        User u = userService.findByUsername(userVO.getUsername());
        if (u != null) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("用户名重复");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        //保存至数据库
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        user.setId(UUID.randomUUID().toString());
        user.setEnabled(1);
        user.setDeletable(1);
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("注册成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
