package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.Property;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Property)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-08-22 16:43:20
 */
@Repository
public interface PropertyDAO {

    /**
     * 根据条件统计
     *
     * @param wrapper
     * @return
     */
    int countByCondition(QueryWrapper wrapper);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    Property select(String id);

    /**
     * 条件查询
     *
     * @param wrapper
     * @return
     */
    List<Property> selectByCondition(QueryWrapper wrapper);

    /**
     * 新增
     *
     * @param permission
     * @return
     */
    int insert(Property property);

    /**
     * 批量新增
     *
     * @param permissions
     * @return
     */
    int batchInsert(List<Property> propertys);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    int batchDelete(List<String> ids);

    /**
     * 根据条件删除
     *
     * @param wrapper
     * @return
     */
    int deleteByCondition(QueryWrapper wrapper);

    /**
     * 更新
     *
     * @param permission
     * @return
     */
    int update(Property property);

    /**
     * 批量更新
     *
     * @param permissions
     * @return
     */
    int batchUpdate(List<Property> propertys);

    /**
     * 根据条件更新
     *
     * @param wrapper
     * @return
     */
    int updateByCondition(QueryWrapper wrapper);
}