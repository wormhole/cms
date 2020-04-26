package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;

/**
 * 用户-角色实体类
 *
 * @author 凉衫薄
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRole implements Serializable {

    private static final long serialVersionUID = 5933213236853317418L;
    private String id;
    private String userId;
    private String roleId;
}
