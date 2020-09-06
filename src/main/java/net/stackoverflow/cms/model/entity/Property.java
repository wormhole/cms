package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Property)实体类
 *
 * @author 凉衫薄
 * @since 2020-09-06 13:46:48
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Property implements Serializable {
    private static final long serialVersionUID = 370624976672022396L;

    private String id;
    private String key;
    private String value;
    private Date ts;
}