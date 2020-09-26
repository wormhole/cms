package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Menu)实体类
 *
 * @author 凉衫薄
 * @since 2020-09-26 20:22:30
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Menu implements Serializable {
    private static final long serialVersionUID = 972866041748512245L;

    private String id;
    private String title;
    private String key;
    private String parent;
    private Date ts;
}