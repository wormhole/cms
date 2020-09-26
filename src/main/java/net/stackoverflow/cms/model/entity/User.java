package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (User)实体类
 *
 * @author 凉衫薄
 * @since 2020-09-26 20:22:57
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 363998374717952797L;

    private String id;
    private String username;
    private String password;
    private String email;
    private String telephone;
    private Integer enable;
    private Integer builtin;
    private Integer ttl;
    private Integer limit;
    private Integer lock;
    private Integer failure;
    private Date ts;
}