package net.stackoverflow.cms.web.controller;

import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.pojo.entity.User;
import net.stackoverflow.cms.pojo.vo.UserVO;
import net.stackoverflow.cms.security.CmsMd5PasswordEncoder;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.ValidateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 用户注册Controller
 *
 * @author 凉衫薄
 */
@Controller
public class RegisterController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param userVO
     * @param session
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity register(@RequestBody UserVO userVO, HttpSession session) {

        Result result = new Result();
        try {
            //校验验证码
            String vcode = (String) session.getAttribute("vcode");
            if (!vcode.equalsIgnoreCase(userVO.getVcode())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("验证码错误");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            //校验数据
            if (!ValidateUtils.validateUsername(userVO.getUsername())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("用户名不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                List<User> users = userService.selectByCondition(new HashMap<String, Object>(16) {{
                    put("username", userVO.getUsername());
                }});
                if (users.size() != 0) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("用户名重复");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
            }
            if (!ValidateUtils.validateTelephone(userVO.getTelephone())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("电话号码格式错误");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            if (!ValidateUtils.validateEmail(userVO.getEmail())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("邮箱格式错误");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            if (!ValidateUtils.validatePassword(userVO.getPassword())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("密码长度不能小于6");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            //保存至数据库
            User user = new User();
            BeanUtils.copyProperties(userVO, user);
            user.setId(UUID.randomUUID().toString());
            user.setEnabled(1);
            user.setPassword(new CmsMd5PasswordEncoder().encode(user.getPassword()));
            userService.insert(user);

            result.setStatus(Result.Status.SUCCESS);
            result.setMessage("注册成功");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("服务器错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
