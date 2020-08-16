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

    @NotBlank(message = "id不能为空", groups = {Admin.class})
    private String id;
    @NotBlank(message = "旧密码不能为空", groups = {Personal.class})
    private String oldPassword;
    @Size(min = 6, message = "新密码长度不不能小于6", groups = {Admin.class, Personal.class})
    private String newPassword;

    public interface Admin {
    }

    public interface Personal {
    }
}
