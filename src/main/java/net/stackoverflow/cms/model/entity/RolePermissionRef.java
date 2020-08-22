package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (RolePermissionRef)实体类
 *
 * @author minsheng.cai
 * @since 2020-08-22 11:42:31
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RolePermissionRef implements Serializable {
    private static final long serialVersionUID = -39633822972018705L;

    private String id;
    private String roleId;
    private String permissionId;
    private Date ts;
}