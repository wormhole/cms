package net.stackoverflow.cms.model.vo;

import lombok.*;

import java.util.List;

/**
 * 分配角色VO
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GrantRoleVO {

    private String userId;
    private List<String> roleIds;
}
