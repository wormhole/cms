package net.stackoverflow.cms.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * 分页参数和查询条件
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
public class Page {

    private Integer page;
    private Integer limit;
    private Integer offset;
    private Map<String, Object> searchParam;

    public Page(Integer page, Integer offset) {
        this.page = page;
        this.offset = offset;
        this.offset = (page - 1) * limit;
    }

    public Page(Integer page, Integer offset, Map<String, Object> searchParam) {
        this.page = page;
        this.offset = offset;
        this.searchParam = searchParam;
        this.offset = (page - 1) * limit;
    }
}
