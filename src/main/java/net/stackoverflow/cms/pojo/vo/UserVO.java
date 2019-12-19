package net.stackoverflow.cms.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户VO类
 *
 * @author 凉衫薄
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {

    private String username;
    private String password;
    private String telephone;
    private String email;
    private String vcode;

}
