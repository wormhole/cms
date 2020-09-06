package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (RoleMenuRef)实体类
 *
 * @author 凉衫薄
 * @since 2020-09-06 13:47:12
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleMenuRef implements Serializable {
    private static final long serialVersionUID = 676437746383170987L;

    private String id;
    private String roleId;
    private String menuId;
    private Date ts;
}