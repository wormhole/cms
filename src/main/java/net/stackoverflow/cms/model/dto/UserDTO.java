package net.stackoverflow.cms.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
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
    @Min(value = 1, message = "ttl不能小于1", groups = {Update.class, Insert.class})
    private Integer ttl;
    @Min(value = 1, message = "limit不能小于1", groups = {Update.class, Insert.class})
    private Integer limit;
    @Min(value = 3, message = "lock不能小于1", groups = {Update.class, Insert.class})
    private Integer lock;
    @Min(value = 3, message = "failure不能小于3", groups = {Update.class, Insert.class})
    private Integer failure;
    @Size(min = 6, message = "密码长度不能小于6", groups = {Insert.class})
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ts;
    private Integer enable;
    private Integer builtin;
    private List<RoleDTO> roles;

    public interface Update {
    }

    public interface Insert {
    }
}
