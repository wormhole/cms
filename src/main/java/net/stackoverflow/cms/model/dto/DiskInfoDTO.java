package net.stackoverflow.cms.model.dto;

import lombok.*;

/**
 * 磁盘容量信息dto类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DiskInfoDTO {
    private Long total;
    private Long free;
    private Long used;
}
