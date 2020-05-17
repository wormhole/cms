package net.stackoverflow.cms.model.vo;

import lombok.*;

import javax.validation.constraints.NotBlank;

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

    @NotBlank(message = "key不能为空")
    private String key;
    @NotBlank(message = "value不能为空")
    private String value;
}
