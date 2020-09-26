package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Role)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-09-26 20:22:36
 */
@Repository
public interface RoleDAO {

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
    Role select(String id);

    /**
     * 查询所有
     *
     * @return
     */
    List<Role> selectAll();

    /**
     * 条件查询
     *
     * @param wrapper
     * @return
     */
    List<Role> selectWithQuery(QueryWrapper wrapper);

    /**
     * 新增
     *
     * @param role
     * @return
     */
    int insert(Role role);

    /**
     * 批量新增
     *
     * @param roles
     * @return
     */
    int batchInsert(List<Role> roles);

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
     * @param role
     * @return
     */
    int update(Role role);

    /**
     * 批量更新
     *
     * @param roles
     * @return
     */
    int batchUpdate(List<Role> roles);

    /**
     * 根据条件更新
     *
     * @param wrapper
     * @return
     */
    int updateWithQuery(QueryWrapper wrapper);
}