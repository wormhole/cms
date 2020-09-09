package net.stackoverflow.cms.common;

import lombok.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 条件查询
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class QueryWrapper {
    /**
     * 每页大小
     */
    private Integer limit;
    /**
     * 偏移量
     */
    private Integer offset;
    /**
     * 排序条件
     */
    private Map<String, String> sortWrapper;
    /**
     * 相等条件
     */
    private Map<String, Object> eqWrapper;
    /**
     * 不相等条件
     */
    private Map<String, Object> neqWrapper;
    /**
     * in条件
     */
    private Map<String, List<?>> inWrapper;
    /**
     * not in条件
     */
    private Map<String, List<?>> ninWrapper;
    /**
     * 模糊查询条件
     */
    private Map<String, List<String>> keyWrapper;
    /**
     * 更新字段
     */
    private Map<String, Object> setWrapper;

    public static QueryWrapperBuilder newBuilder() {
        return new QueryWrapperBuilder();
    }

    /**
     * QueryWrapperBuilder 建造者
     */
    public static class QueryWrapperBuilder {

        private Integer limit;
        private Integer offset;
        private final Map<String, String> sortWrapper;
        private final Map<String, Object> eqWrapper;
        private final Map<String, Object> neqWrapper;
        private final Map<String, List<?>> inWrapper;
        private final Map<String, List<?>> ninWrapper;
        private final Map<String, List<String>> keyWrapper;
        private final Map<String, Object> setWrapper;

        public QueryWrapperBuilder() {
            sortWrapper = new LinkedHashMap<>();
            eqWrapper = new HashMap<>();
            neqWrapper = new HashMap<>();
            inWrapper = new HashMap<>();
            ninWrapper = new HashMap<>();
            keyWrapper = new HashMap<>();
            setWrapper = new HashMap<>();
        }

        /**
         * 添加字段eq条件
         *
         * @param condition 条件
         * @param column    字段名
         * @param value     值
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder eq(Boolean condition, String column, Object value) {
            if (condition) {
                eqWrapper.put(column, value);
            }
            return this;
        }

        /**
         * 添加字段eq条件
         *
         * @param column 字段名
         * @param value  值
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder eq(String column, Object value) {
            return eq(true, column, value);
        }

        /**
         * 添加字段eq条件
         *
         * @param condition 条件
         * @param column    字段名
         * @param value     值
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder neq(Boolean condition, String column, Object value) {
            if (condition) {
                neqWrapper.put(column, value);
            }
            return this;
        }

        /**
         * 添加字段eq条件
         *
         * @param column 字段名
         * @param value  值
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder neq(String column, Object value) {
            return neq(true, column, value);
        }

        /**
         * 添加字段in条件
         *
         * @param condition 条件
         * @param column    字段名
         * @param value     值
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder in(Boolean condition, String column, List<?> value) {
            if (condition) {
                inWrapper.put(column, value);
            }
            return this;
        }

        /**
         * 添加字段in条件
         *
         * @param column 字段名
         * @param value  值
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder in(String column, List<?> value) {
            return in(true, column, value);
        }

        /**
         * 添加字段not in条件
         *
         * @param condition 条件
         * @param column    字段名
         * @param value     值
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder notIn(Boolean condition, String column, List<?> value) {
            if (condition) {
                ninWrapper.put(column, value);
            }
            return this;
        }

        /**
         * 添加字段not in条件
         *
         * @param column 字段名
         * @param value  值
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder notIn(String column, List<?> value) {
            return notIn(true, column, value);
        }

        /**
         * 添加模糊查询条件
         *
         * @param condition 条件
         * @param key       关键字
         * @param columns   字段
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder like(Boolean condition, String key, List<String> columns) {
            if (condition) {
                keyWrapper.put(key, columns);
            }
            return this;
        }

        /**
         * 添加模糊查询条件
         *
         * @param key     关键字
         * @param columns 字段
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder like(String key, List<String> columns) {
            return like(true, key, columns);
        }

        /**
         * 分页条件
         *
         * @param condition 条件
         * @param offset    偏移量
         * @param limit     每页大小
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder page(Boolean condition, Integer offset, Integer limit) {
            if (condition) {
                this.offset = offset;
                this.limit = limit;
            }
            return this;
        }

        /**
         * 分页条件
         *
         * @param offset 偏移量
         * @param limit  每页大小
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder page(Integer offset, Integer limit) {
            return page(true, offset, limit);
        }

        /**
         * 顺序排序
         *
         * @param column 排序字段
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder asc(String column) {
            return asc(true, column);
        }

        /**
         * 顺序排序
         *
         * @param condition 条件
         * @param column    排序字段
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder asc(Boolean condition, String column) {
            if (condition) {
                sortWrapper.put(column, "asc");
            }
            return this;
        }

        /**
         * 逆序排序
         *
         * @param column 排序字段
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder desc(String column) {
            return desc(true, column);
        }

        /**
         * 逆序排序
         *
         * @param condition 条件
         * @param column    排序字段
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder desc(Boolean condition, String column) {
            if (condition) {
                sortWrapper.put(column, "desc");
            }
            return this;
        }

        /**
         * 自定义排序
         *
         * @param column 排序字段
         * @param order  排序方式
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder sort(String column, String order) {
            return sort(true, column, order);
        }

        /**
         * 自定义排序
         *
         * @param condition 条件
         * @param column    排序字段
         * @param order     排序方式
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder sort(Boolean condition, String column, String order) {
            if (condition) {
                sortWrapper.put(column, order);
            }
            return this;
        }

        /**
         * 更新操作中，需要更新的字段的值
         *
         * @param condition 条件
         * @param column    需要更新的字段
         * @param value     需要更新的值
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder set(Boolean condition, String column, Object value) {
            if (condition) {
                setWrapper.put(column, value);
            }
            return this;
        }

        /**
         * 更新操作中，需要更新的字段的值
         *
         * @param column 需要更新的字段
         * @param value  需要更新的值
         * @return 返回建造者对象
         */
        public synchronized QueryWrapperBuilder set(String column, Object value) {
            return set(true, column, value);
        }

        /**
         * 构建QueryWrapper对象
         *
         * @return 返回QueryWrapper对象
         */
        public QueryWrapper build() {
            return new QueryWrapper(limit, offset, sortWrapper, eqWrapper, neqWrapper, inWrapper, ninWrapper, keyWrapper, setWrapper);
        }
    }
}

