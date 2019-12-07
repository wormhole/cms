package net.stackoverflow.cms.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户实体类
 *
 * @author 凉衫薄
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String username;
    private String password;
    private String salt;
    private Integer enabled;
    private String email;
    private String telephone;

}
