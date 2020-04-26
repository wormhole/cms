package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * token实体类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Token implements Serializable {

    private static final long serialVersionUID = 8827053391043133931L;
    private String id;
    private String token;
    private Date lastUsed;
    private String userId;

}
