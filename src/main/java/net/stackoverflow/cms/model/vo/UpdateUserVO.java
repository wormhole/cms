package net.stackoverflow.cms.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户信息更新VO
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserVO {

    private String id;
    private String username;
    private String email;
    private String telephone;
    private String password;
    /**
     * 类型：0-更新基本信息，1-更新密码
     */
    private Integer type;
}
