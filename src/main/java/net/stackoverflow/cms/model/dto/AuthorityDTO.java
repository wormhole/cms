package net.stackoverflow.cms.model.dto;

import lombok.*;

import java.util.List;

/**
 * 用户菜单权限及基础信息
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthorityDTO {

    private String username;
    private List<String> roles;
    private List<String> permissions;
}
