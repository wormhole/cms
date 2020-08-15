package net.stackoverflow.cms.common;

import lombok.*;

import java.util.List;

/**
 * 分页响应对象
 *
 * @param <T>
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageResponse<T> {

    private Integer total;
    private List<T> list;
}
