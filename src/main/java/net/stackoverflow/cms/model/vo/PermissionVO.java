package net.stackoverflow.cms.model.vo;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * 权限VO类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PermissionVO {

    @NotBlank(message = "id不能为空", groups = {Update.class})
    private String id;
    @NotBlank(message = "name不能为空", groups = {Insert.class, Update.class})
    private String name;
    @NotBlank(message = "description不能为空", groups = {Insert.class, Update.class})
    private String description;
    private Integer deletable;

    public interface Update {
    }

    public interface Insert {
    }
}
