package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (UserRoleRef)实体类
 *
 * @author 凉衫薄
 * @since 2020-09-26 20:23:02
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRoleRef implements Serializable {
    private static final long serialVersionUID = -55377608861316669L;

    private String id;
    private String userId;
    private String roleId;
    private Date ts;
}