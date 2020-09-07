package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.dto.UserDTO;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 个人主页接口
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping("/personal")
@Slf4j
@Validated
public class PersonalController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<Result<UserDTO>> getSelf() {
        User user = userService.findById(super.getUserId());
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        dto.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 更新用户信息
     *
     * @param dto 用户信息dto对象
     * @return
     */
    @PutMapping
    public ResponseEntity<Result<Object>> update(@RequestBody @Validated(UserDTO.Update.class) UserDTO dto) {
        if (!super.getUserId().equals(dto.getId())) {
            throw new BusinessException("不允许更新其他用户信息");
        }
        userService.updateBase(dto);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }
}
