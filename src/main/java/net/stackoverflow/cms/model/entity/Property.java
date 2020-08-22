package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Property)实体类
 *
 * @author 凉衫薄
 * @since 2020-08-22 16:49:13
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Property implements Serializable {
    private static final long serialVersionUID = 724838538199350251L;

    private String id;
    private String key;
    private String value;
    private Date ts;
}