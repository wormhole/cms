package net.stackoverflow.cms.common;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * 分页参数
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
    private String order = "id";
    private String sort = "asc";
    private Map<String, Object> searchMap;
    private Map<String, Object> likeMap;

    public Page(Integer page, Integer limit) {
        this.page = page;
        this.limit = limit;
        this.offset = (page - 1) * limit;
    }

    public Page(Integer page, Integer limit, String order, String sort) {
        this.page = page;
        this.limit = limit;
        this.offset = (page - 1) * limit;
        if (StringUtils.isNotBlank(order)) {
            this.order = order;
        }
        if (StringUtils.isNotBlank(sort)) {
            this.sort = sort;
        }
    }

    public Page(Integer page, Integer limit, Map<String, Object> searchMap, Map<String, Object> likeMap) {
        this.page = page;
        this.limit = limit;
        this.offset = (page - 1) * limit;
        this.searchMap = searchMap;
        this.likeMap = likeMap;
    }
}
