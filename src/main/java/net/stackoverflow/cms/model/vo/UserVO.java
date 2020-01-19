package net.stackoverflow.cms.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
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
public class UserVO implements Serializable {

    private String id;
    private String username;
    private String email;
    private String telephone;
    private Integer enabled;
    private Integer deletable;
    private String password;
    private List<RoleVO> roles;
    private String vcode;
    /**
     * 操作类型：0-更新基本信息，1-更新密码
     */
    private Integer type;
}
