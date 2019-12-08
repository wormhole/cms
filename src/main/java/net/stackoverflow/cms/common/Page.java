package net.stackoverflow.cms.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

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

    public Page(Integer page, Integer limit, Map<String, Object> searchParam) {
        this.page = page;
        this.limit = limit;
        this.offset = (page - 1) * limit;
        this.searchMap = searchMap;
    }
}
