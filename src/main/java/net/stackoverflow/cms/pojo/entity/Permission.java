package net.stackoverflow.cms.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Permission implements Serializable {

    private String id;
    private String name;
    private String url;
    private String description;
}
