package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Menu)实体类
 *
 * @author 凉衫薄
 * @since 2020-09-06 10:09:34
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Menu implements Serializable {
    private static final long serialVersionUID = -46171317154920145L;

    private String id;
    private String title;
    private String key;
    private String parent;
    private Date ts;
}