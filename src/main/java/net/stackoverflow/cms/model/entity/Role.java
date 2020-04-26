package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;

/**
 * 角色实体类
 *
 * @author 凉衫薄
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role implements Serializable {

    private static final long serialVersionUID = -6600872198532100239L;
    private String id;
    private String name;
    private String description;
    private Integer deletable;
}
