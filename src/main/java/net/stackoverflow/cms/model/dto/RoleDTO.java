package net.stackoverflow.cms.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 角色VO类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleDTO {

    @NotBlank(message = "id不能为空", groups = Update.class)
    private String id;
    @NotBlank(message = "name不能为空", groups = {Update.class, Insert.class})
    private String name;
    @NotBlank(message = "note不能为空", groups = {Update.class, Insert.class})
    private String note;
    List<PermissionDTO> permissions;
    private Integer builtin;
    private Date ts;

    public interface Insert {
    }

    public interface Update {
    }
}
