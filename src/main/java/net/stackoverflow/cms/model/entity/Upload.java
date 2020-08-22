package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Upload)实体类
 *
 * @author 凉衫薄
 * @since 2020-08-22 16:43:39
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Upload implements Serializable {
    private static final long serialVersionUID = 708079850157148331L;

    private String id;
    private String name;
    private String path;
    private Date ts;
    private String userId;
    private Integer type;
}