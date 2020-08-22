package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-08-22 16:49:40
 */
@Repository
public interface UserDAO {

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
    User select(String id);

    /**
     * 条件查询
     *
     * @param wrapper
     * @return
     */
    List<User> selectByCondition(QueryWrapper wrapper);

    /**
     * 新增
     *
     * @param user
     * @return
     */
    int insert(User user);

    /**
     * 批量新增
     *
     * @param users
     * @return
     */
    int batchInsert(List<User> users);

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
     * @param user
     * @return
     */
    int update(User user);

    /**
     * 批量更新
     *
     * @param users
     * @return
     */
    int batchUpdate(List<User> users);

    /**
     * 根据条件更新
     *
     * @param wrapper
     * @return
     */
    int updateByCondition(QueryWrapper wrapper);
}