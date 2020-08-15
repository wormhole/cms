package net.stackoverflow.cms.model.dto;

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
public class PropertyDTO {

    @NotBlank(message = "key不能为空")
    private String key;
    @NotBlank(message = "value不能为空")
    private String value;
}
