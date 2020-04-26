package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;

/**
 * 权限实体类
 *
 * @author 凉衫薄
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Permission implements Serializable {

    private static final long serialVersionUID = -7269492718613757063L;
    private String id;
    private String name;
    private String description;
    private Integer deletable;
}
