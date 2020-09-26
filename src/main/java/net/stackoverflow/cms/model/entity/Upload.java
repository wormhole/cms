package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Upload)实体类
 *
 * @author 凉衫薄
 * @since 2020-09-26 20:22:50
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Upload implements Serializable {
    private static final long serialVersionUID = -45179264542595732L;

    private String id;
    private String name;
    private String path;
    private Date ts;
    private String userId;
    private Integer type;
}