package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (UserRoleRef)实体类
 *
 * @author 凉衫薄
 * @since 2020-09-06 13:28:36
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRoleRef implements Serializable {
    private static final long serialVersionUID = 670211048267124993L;

    private String id;
    private String userId;
    private String roleId;
    private Date ts;
}