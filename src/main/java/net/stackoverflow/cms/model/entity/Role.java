package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Role)实体类
 *
 * @author 凉衫薄
 * @since 2020-08-15 10:27:35
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role implements Serializable {
    private static final long serialVersionUID = 831711235944440035L;

    private String id;
    private String name;
    private String note;
    private Integer builtin;
    private Date ts;
}