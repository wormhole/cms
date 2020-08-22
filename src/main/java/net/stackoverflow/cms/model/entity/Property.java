package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Property)实体类
 *
 * @author minsheng.cai
 * @since 2020-08-22 11:42:09
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Property implements Serializable {
    private static final long serialVersionUID = 864453131150283541L;

    private String id;
    private String key;
    private String value;
    private Date ts;
}