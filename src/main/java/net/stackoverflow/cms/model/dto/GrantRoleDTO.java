package net.stackoverflow.cms.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分配角色VO
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GrantRoleDTO {

    @NotBlank(message = "userId不能为空")
    private String userId;
    @NotNull(message = "roleIds不能为null")
    private List<String> roleIds;
}
