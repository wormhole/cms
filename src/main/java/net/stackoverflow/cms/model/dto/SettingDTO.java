package net.stackoverflow.cms.model.dto;

import lombok.*;

/**
 * 系统设置信息dto类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SettingDTO {

    private String title;
    private String copyright;
    private String head;
}
