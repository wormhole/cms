package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * (Upload)实体类
 *
 * @author minsheng.cai
 * @since 2020-08-22 13:34:16
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Upload implements Serializable {
    private static final long serialVersionUID = 679097575980561368L;

    private String id;
    private String name;
    private String path;
    private Date ts;
    private String userId;
    private Integer type;
}