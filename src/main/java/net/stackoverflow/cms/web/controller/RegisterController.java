package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.dto.UserDTO;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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

    /**
     * 用户注册接口
     *
     * @param userDTO
     * @param session
     * @return
     */
    @PostMapping(value = "/register")
    public ResponseEntity<Result<Object>> register(@RequestBody @Validated(UserDTO.Insert.class) UserDTO userDTO, HttpSession session) {
        //校验验证码
        String code = (String) session.getAttribute("code");
        if (!code.equalsIgnoreCase(userDTO.getCode())) {
            throw new BusinessException("验证码错误");
        }

        //保存至数据库
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("注册成功"));

    }
}
