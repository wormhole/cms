package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (User)实体类
 *
 * @author 凉衫薄
 * @since 2020-08-22 16:43:44
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 448424014267206927L;

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