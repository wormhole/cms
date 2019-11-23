package net.stackoverflow.cms.common;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Page {

    private Integer page;
    private Integer limit;
    private Integer offset;
    private String order;
    private String sort;
    private String key;
    private Map<String, Object> searchMap;

    public Page(Integer page, Integer limit) {
        this.page = page;
        this.limit = limit;
        this.offset = (page - 1) * limit;
    }

    public Page(Integer page, Integer limit, String order, String sort) {
        this.page = page;
        this.limit = limit;
        this.offset = (page - 1) * limit;
        this.order = order;
        this.sort = sort;
    }

    public Page(Integer page, Integer limit, String order, String sort, String key) {
        this.page = page;
        this.limit = limit;
        this.offset = (page - 1) * limit;
        this.order = order;
        this.sort = sort;
        this.key = key;
    }

    public Page(Integer page, Integer limit, String key) {
        this.page = page;
        this.limit = limit;
        this.offset = (page - 1) * limit;
        this.key = key;
    }

    public Page(Integer page, Integer limit, Map<String, Object> searchMap) {
        this.page = page;
        this.limit = limit;
        this.offset = (page - 1) * limit;
        this.searchMap = searchMap;
    }
}
