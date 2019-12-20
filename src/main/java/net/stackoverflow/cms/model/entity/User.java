package net.stackoverflow.cms.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author 凉衫薄
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private String id;
    private String username;
    private String password;
    private String email;
    private String telephone;
    private Integer enabled;
    private Integer deletable;

}
