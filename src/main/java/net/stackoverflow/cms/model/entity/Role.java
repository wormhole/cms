package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Role)实体类
 *
 * @author 凉衫薄
 * @since 2020-08-22 16:49:19
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role implements Serializable {
    private static final long serialVersionUID = 299347335366459470L;

    private String id;
    private String name;
    private String note;
    private Integer builtin;
    private Date ts;
}