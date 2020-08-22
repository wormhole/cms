package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (RolePermissionRef)实体类
 *
 * @author 凉衫薄
 * @since 2020-08-22 16:43:32
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RolePermissionRef implements Serializable {
    private static final long serialVersionUID = -46356313630199754L;

    private String id;
    private String roleId;
    private String permissionId;
    private Date ts;
}