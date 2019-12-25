package net.stackoverflow.cms.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 权限VO类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionVO implements Serializable {

    private String id;
    private String name;
    private String description;
    private Integer deletable;
}
