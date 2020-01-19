package net.stackoverflow.cms.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class File implements Serializable {

    private String id;
    private String name;
    private String path;
    private Date time;
    private String userId;
}
