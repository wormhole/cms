package net.stackoverflow.cms.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 密码VO类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswordDTO {

    @Size(min = 6, message = "新密码长度不不能小于6")
    private String password;
    @NotBlank(message = "旧密码不能为空")
    private String old;
}
