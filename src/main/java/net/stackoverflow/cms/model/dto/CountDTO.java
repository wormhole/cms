package net.stackoverflow.cms.model.dto;

import lombok.*;

/**
 * 首页统计数量
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CountDTO {

    private Integer user;
    private Integer role;
    private Integer file;
    private Integer menu;
}
