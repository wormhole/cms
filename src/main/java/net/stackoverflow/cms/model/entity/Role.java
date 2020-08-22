package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Role)实体类
 *
 * @author minsheng.cai
 * @since 2020-08-22 11:42:20
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role implements Serializable {
    private static final long serialVersionUID = -25018201199330255L;

    private String id;
    private String name;
    private String note;
    private Integer builtin;
    private Date ts;
}