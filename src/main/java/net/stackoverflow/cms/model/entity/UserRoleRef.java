package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (UserRoleRef)实体类
 *
 * @author minsheng.cai
 * @since 2020-08-22 13:34:44
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRoleRef implements Serializable {
    private static final long serialVersionUID = -72287444393428800L;

    private String id;
    private String userId;
    private String roleId;
    private Date ts;
}