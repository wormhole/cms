package net.stackoverflow.cms.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Role implements Serializable {

    private String id;
    private String name;
    private String description;
}
