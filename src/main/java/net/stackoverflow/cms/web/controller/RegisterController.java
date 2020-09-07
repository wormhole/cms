package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/register")
@Slf4j
@Validated
public class RegisterController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param dto     用户信息dto对象
     * @param captcha 验证码
     * @param session 会话对象
     * @return
     */
    @PostMapping
    public ResponseEntity<Result<Object>> register(@RequestBody @Validated(UserDTO.Insert.class) UserDTO dto, @NotBlank(message = "验证码不能为空") @PathParam("captcha") String captcha, HttpSession session) {
        //校验验证码
        String code = (String) session.getAttribute("captcha");
        if (!captcha.equalsIgnoreCase(code)) {
            throw new BusinessException("验证码错误");
        }

        //保存至数据库
        dto.setTtl(30);
        dto.setLimit(1);
        dto.setLock(30);
        dto.setFailure(5);
        userService.save(dto);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("注册成功"));
    }
}
