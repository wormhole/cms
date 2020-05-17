package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 上传文件实体类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class File implements Serializable {

    private static final long serialVersionUID = -3140934668795125212L;
    private String id;
    private String name;
    private String path;
    private Date time;
    private String userId;
    private Integer type;
}
