package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author minsheng.cai
 * @since 2020-08-22 13:34:32
 */
@Repository
public interface UserDAO {

    int countByCondition(QueryWrapper wrapper);

    User select(String id);

    List<User> selectByCondition(QueryWrapper wrapper);

    int insert(User user);

    int batchInsert(List<User> users);

    int delete(String id);

    int batchDelete(List<String> ids);

    int deleteByCondition(QueryWrapper wrapper);

    int update(User user);

    int batchUpdate(List<User> users);

    int updateByCondition(QueryWrapper wrapper);
}