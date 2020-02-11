package net.stackoverflow.cms.web.controller.personal;

import io.micrometer.core.instrument.util.StringUtils;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/personal")
@Slf4j
public class PersonalController extends BaseController {

    @Autowired
    private UserService userService;

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

        //校验参数
        if (userVO.getType() != 0 && userVO.getType() != 1) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("类型错误");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        User user = userService.findById(getUserDetails().getId());

        if (userVO.getType() == 0) {
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
        } else if (userVO.getType() == 1) {
            if (!validatePassword(userVO.getPassword())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("密码长度不能小于6");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            String password = new CmsMd5PasswordEncoder().encode(userVO.getPassword());
            user.setPassword(password);
            userService.update(user);
        }
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
