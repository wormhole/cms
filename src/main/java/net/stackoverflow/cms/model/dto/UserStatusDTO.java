package net.stackoverflow.cms.model.dto;

import lombok.*;

/**
 * 用户状态
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserStatusDTO {

    private Integer online;
    private Integer lock;
    private Integer enable;
    private Integer disable;
    private Integer total;
}
