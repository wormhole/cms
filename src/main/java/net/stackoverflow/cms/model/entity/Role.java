package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Role)实体类
 *
 * @author 凉衫薄
 * @since 2020-09-26 20:22:36
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role implements Serializable {
    private static final long serialVersionUID = -96958659500495549L;

    private String id;
    private String name;
    private String note;
    private Integer builtin;
    private Date ts;
}