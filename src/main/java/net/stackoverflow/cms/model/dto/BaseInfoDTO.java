package net.stackoverflow.cms.model.dto;

import lombok.*;

/**
 * 基础信息dto类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseInfoDTO {

    private String title;
    private String head;
    private String copyright;
}
