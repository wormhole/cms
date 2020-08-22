package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Permission)实体类
 *
 * @author minsheng.cai
 * @since 2020-08-22 11:41:59
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Permission implements Serializable {
    private static final long serialVersionUID = 687451922102801956L;

    private String id;
    private String name;
    private String note;
    private Integer builtin;
    private Date ts;
}