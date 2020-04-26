package net.stackoverflow.cms.model.vo;

import lombok.*;

import java.util.List;

/**
 * 用户VO类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserVO {

    private String id;
    private String username;
    private String email;
    private String telephone;
    private Integer enabled;
    private Integer deletable;
    private String password;
    private List<RoleVO> roles;
    private String vcode;
    private String role;
}
