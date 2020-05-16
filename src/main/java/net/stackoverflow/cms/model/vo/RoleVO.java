package net.stackoverflow.cms.model.vo;

import lombok.*;

import javax.validation.constraints.NotBlank;
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
public class RoleVO {

    @NotBlank(message = "id不能为空", groups = Update.class)
    private String id;
    @NotBlank(message = "name不能为空", groups = {Update.class, Insert.class})
    private String name;
    @NotBlank(message = "description不能为空", groups = {Update.class, Insert.class})
    private String description;
    List<PermissionVO> permissions;
    private Integer deletable;

    public interface Insert {
    }

    public interface Update {
    }
}
