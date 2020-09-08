package net.stackoverflow.cms.model.dto;

import lombok.*;

/**
 * 内存信息dto类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemInfoDTO {

    private Double total;
    private Double free;
    private Double used;
}
