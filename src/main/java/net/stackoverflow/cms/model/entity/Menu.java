package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Menu)实体类
 *
 * @author 凉衫薄
 * @since 2020-09-04 23:25:06
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Menu implements Serializable {
    private static final long serialVersionUID = -96187383734744659L;

    private String id;
    private String title;
    private String key;
    private String parent;
    private Date ts;
}