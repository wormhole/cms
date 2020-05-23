package net.stackoverflow.cms.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.stackoverflow.cms.util.StringUtils;

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
    private String order = "asc";
    private String sort = "id";
    private Map<String, Object> condition;
    private String key;

    public Page(Integer page, Integer limit) {
        this.page = page;
        this.limit = limit;
        this.offset = (page - 1) * limit;
    }

    public Page(Integer page, Integer limit, String sort, String order, Map<String, Object> condition, String key) {
        this.page = page;
        this.limit = limit;
        if (page != null && limit != null) {
            this.offset = (page - 1) * limit;
        }
        if (StringUtils.isNotEmpty(order)) {
            this.order = order;
        }
        if (StringUtils.isNotEmpty(sort)) {
            this.sort = sort;
        }
        this.condition = condition;
        if (StringUtils.isNotEmpty(key)) {
            this.key = key;
        }
    }
}
