package net.stackoverflow.cms.model.vo;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分配权限VO类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GrantPermissionVO {

    @NotBlank(message = "roleId不能为空")
    private String roleId;
    @NotNull(message = "permissionIds不能为null")
    private List<String> permissionIds;
}
