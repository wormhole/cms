package net.stackoverflow.cms.web.controller.personal;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.PasswordDTO;
import net.stackoverflow.cms.model.dto.UserDTO;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    @GetMapping(value = "/user")
    public ResponseEntity<Result<UserDTO>> user() {
        User user = userService.findById(super.getUserId());
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", userDTO));
    }


    /**
     * 更新个人信息
     *
     * @param dto
     * @return
     */
    @PutMapping(value = "/user")
    public ResponseEntity<Result<Object>> update(@RequestBody @Validated(UserDTO.Update.class) UserDTO dto) {
        dto.setId(super.getUserId());
        userService.updateBase(dto);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));
    }

    /**
     * 修改密码
     *
     * @param dto
     * @return
     */
    @PutMapping(value = "/user/password")
    public ResponseEntity<Result<Object>> password(@RequestBody @Validated(PasswordDTO.Personal.class) PasswordDTO dto) {
        if (!dto.getNewPassword().equals(dto.getCheckPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body(Result.failure("两次密码不一致"));
        }
        userService.updatePassword(super.getUserId(), dto.getOldPassword(), dto.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("修改密码成功"));
    }
}
