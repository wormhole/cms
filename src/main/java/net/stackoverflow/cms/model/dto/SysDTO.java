package net.stackoverflow.cms.model.dto;

import lombok.*;

/**
 * 系统信息
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysDTO {

    private String title;
    private String head;
    private String copyright;
}
