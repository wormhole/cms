package net.stackoverflow.cms.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

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
public class PermissionDTO {

    @NotBlank(message = "id不能为空", groups = {Update.class})
    private String id;
    @NotBlank(message = "name不能为空", groups = {Insert.class, Update.class})
    private String name;
    @NotBlank(message = "note不能为空", groups = {Insert.class, Update.class})
    private String note;
    private Integer builtin;
    private Date ts;

    public interface Update {
    }

    public interface Insert {
    }
}
