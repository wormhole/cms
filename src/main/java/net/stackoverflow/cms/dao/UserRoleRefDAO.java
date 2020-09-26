package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.UserRoleRef;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (UserRoleRef)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-09-26 20:23:02
 */
@Repository
public interface UserRoleRefDAO {

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
    UserRoleRef select(String id);

    /**
     * 查询所有
     *
     * @return
     */
    List<UserRoleRef> selectAll();

    /**
     * 条件查询
     *
     * @param wrapper
     * @return
     */
    List<UserRoleRef> selectWithQuery(QueryWrapper wrapper);

    /**
     * 新增
     *
     * @param userRoleRef
     * @return
     */
    int insert(UserRoleRef userRoleRef);

    /**
     * 批量新增
     *
     * @param userRoleRefs
     * @return
     */
    int batchInsert(List<UserRoleRef> userRoleRefs);

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
     * @param userRoleRef
     * @return
     */
    int update(UserRoleRef userRoleRef);

    /**
     * 批量更新
     *
     * @param userRoleRefs
     * @return
     */
    int batchUpdate(List<UserRoleRef> userRoleRefs);

    /**
     * 根据条件更新
     *
     * @param wrapper
     * @return
     */
    int updateWithQuery(QueryWrapper wrapper);
}