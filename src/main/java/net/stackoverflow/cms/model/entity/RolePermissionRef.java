package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (RolePermissionRef)实体类
 *
 * @author 凉衫薄
 * @since 2020-08-15 10:28:06
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RolePermissionRef implements Serializable {
    private static final long serialVersionUID = 627918688473270967L;

    private String id;
    private String roleId;
    private String permissionId;
    private Date ts;
}