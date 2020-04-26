package net.stackoverflow.cms.web.controller.personal;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.vo.PasswordVO;
import net.stackoverflow.cms.model.vo.UserVO;
import net.stackoverflow.cms.security.CmsMd5PasswordEncoder;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/personal")
@Slf4j
public class PersonalController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private CmsMd5PasswordEncoder encoder;

    /**
     * 获取个人信息
     *
     * @return
     */
    @GetMapping(value = "/info")
    public ResponseEntity info() {
        Result result = new Result();

        User user = userService.findById(getUserDetails().getId());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setPassword(null);
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        result.setData(userVO);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }


    /**
     * 更新个人信息
     *
     * @param userVO
     * @return
     */
    @PutMapping(value = "/update")
    public ResponseEntity update(@RequestBody UserVO userVO) {

        Result result = new Result();

        User user = userService.findById(getUserDetails().getId());

        if (StringUtils.isBlank(userVO.getUsername())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("用户名不能为空");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            if (!user.getUsername().equals(userVO.getUsername())) {
                User users = userService.findByUsername(userVO.getUsername());
                if (users != null) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("用户名已存在");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
            }
        }
        if (!validateEmail(userVO.getEmail())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("邮箱格式错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        if (!validateTelephone(userVO.getTelephone())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("电话号码格式错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        user.setUsername(userVO.getUsername());
        user.setEmail(userVO.getEmail());
        user.setTelephone(userVO.getTelephone());
        userService.update(user);
        result.setData(user);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 修改密码
     *
     * @param passwordVO
     * @return
     */
    @PutMapping(value = "/password")
    public ResponseEntity password(@RequestBody PasswordVO passwordVO) {
        Result result = new Result();

        if (StringUtils.isBlank(passwordVO.getOldPassword()) || StringUtils.isBlank(passwordVO.getNewPassword()) || StringUtils.isBlank(passwordVO.getCheckPassword())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("字段不能为空");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        if (!passwordVO.getNewPassword().equals(passwordVO.getCheckPassword())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("两次密码不一致");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        if (!validatePassword(passwordVO.getNewPassword())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("新密码长度不能小于6");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        User user = userService.findById(getUserDetails().getId());
        if (!encoder.encode(passwordVO.getOldPassword()).equals(user.getPassword())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("旧密码错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        String password = encoder.encode(passwordVO.getNewPassword());
        user.setPassword(password);
        userService.update(user);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("修改成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
