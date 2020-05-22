package net.stackoverflow.cms.web.controller.personal;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.vo.PasswordVO;
import net.stackoverflow.cms.model.vo.UserVO;
import net.stackoverflow.cms.security.CmsMd5PasswordEncoder;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/personal")
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
    public ResponseEntity update(@RequestBody @Validated(UserVO.Update.class) UserVO userVO) {

        Result result = new Result();

        User user = userService.findById(userVO.getId());

        if (!user.getUsername().equals(userVO.getUsername())) {
            User users = userService.findByUsername(userVO.getUsername());
            if (users != null) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("用户名已存在");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
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
    public ResponseEntity password(@RequestBody @Validated(PasswordVO.Personal.class) PasswordVO passwordVO) {
        Result result = new Result();

        if (!passwordVO.getNewPassword().equals(passwordVO.getCheckPassword())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("两次密码不一致");
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
