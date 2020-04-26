package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;

/**
 * 角色-权限实体类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 5191984276531767559L;
    private String id;
    private String roleId;
    private String permissionId;
}
