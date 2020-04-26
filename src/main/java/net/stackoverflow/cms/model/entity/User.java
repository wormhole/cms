package net.stackoverflow.cms.model.entity;

import lombok.*;

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
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = -1176841729258973475L;
    private String id;
    private String username;
    private String password;
    private String email;
    private String telephone;
    private Integer enabled;
    private Integer deletable;

}
