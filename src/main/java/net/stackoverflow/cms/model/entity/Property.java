package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Property)实体类
 *
 * @author 凉衫薄
 * @since 2020-08-22 16:43:20
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Property implements Serializable {
    private static final long serialVersionUID = 751084510270631243L;

    private String id;
    private String key;
    private String value;
    private Date ts;
}