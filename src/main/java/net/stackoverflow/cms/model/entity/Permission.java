package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Permission)实体类
 *
 * @author 凉衫薄
 * @since 2020-08-22 16:43:14
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Permission implements Serializable {
    private static final long serialVersionUID = -82366178180005508L;

    private String id;
    private String name;
    private String note;
    private Integer builtin;
    private Date ts;
}