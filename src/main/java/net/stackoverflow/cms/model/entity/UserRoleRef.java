package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (UserRoleRef)实体类
 *
 * @author 凉衫薄
 * @since 2020-08-22 16:49:46
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRoleRef implements Serializable {
    private static final long serialVersionUID = -72818076082467591L;

    private String id;
    private String userId;
    private String roleId;
    private Date ts;
}