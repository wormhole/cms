package net.stackoverflow.cms.model.vo;

import lombok.*;

import java.util.List;

/**
 * 角色VO类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleVO {

    private String id;
    private String name;
    private String description;
    List<PermissionVO> permissions;
    private Integer deletable;
}
