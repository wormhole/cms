package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.RoleMenuRef;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (RoleMenuRef)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-09-26 20:22:40
 */
@Repository
public interface RoleMenuRefDAO {

    /**
     * 根据条件统计
     *
     * @param wrapper
     * @return
     */
    int countWithQuery(QueryWrapper wrapper);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    RoleMenuRef select(String id);

    /**
     * 查询所有
     *
     * @return
     */
    List<RoleMenuRef> selectAll();

    /**
     * 条件查询
     *
     * @param wrapper
     * @return
     */
    List<RoleMenuRef> selectWithQuery(QueryWrapper wrapper);

    /**
     * 新增
     *
     * @param roleMenuRef
     * @return
     */
    int insert(RoleMenuRef roleMenuRef);

    /**
     * 批量新增
     *
     * @param roleMenuRefs
     * @return
     */
    int batchInsert(List<RoleMenuRef> roleMenuRefs);

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
    int deleteWithQuery(QueryWrapper wrapper);

    /**
     * 更新
     *
     * @param roleMenuRef
     * @return
     */
    int update(RoleMenuRef roleMenuRef);

    /**
     * 批量更新
     *
     * @param roleMenuRefs
     * @return
     */
    int batchUpdate(List<RoleMenuRef> roleMenuRefs);

    /**
     * 根据条件更新
     *
     * @param wrapper
     * @return
     */
    int updateWithQuery(QueryWrapper wrapper);
}