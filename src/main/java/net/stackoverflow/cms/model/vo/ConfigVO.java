package net.stackoverflow.cms.model.vo;

import lombok.*;

/**
 * 配置信息VO
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfigVO {

    private String id;
    private String key;
    private String value;
}
