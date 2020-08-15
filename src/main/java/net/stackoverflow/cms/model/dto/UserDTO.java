package net.stackoverflow.cms.model.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 用户VO类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    @NotBlank(message = "id不能为空", groups = Update.class)
    private String id;
    @NotBlank(message = "username不能为空", groups = {Update.class, Insert.class})
    private String username;
    @Email(message = "email格式错误", groups = {Update.class, Insert.class})
    private String email;
    @Pattern(regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$", message = "电话格式错误", groups = {Update.class, Insert.class})
    private String telephone;
    private Integer enable;
    private Integer builtin;
    private Integer ttl;
    private Integer limit;
    private Integer lock;
    private Integer failure;
    private Date ts;
    @Size(min = 6, message = "密码长度不能小于6", groups = {Insert.class})
    private String password;
    private List<RoleDTO> roles;
    private String code;

    public interface Update {
    }

    public interface Insert {
    }
}
