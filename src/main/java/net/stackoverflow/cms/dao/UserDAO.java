package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDAO {

    List<User> selectByPage(Page page);

    List<User> selectByCondition(Map<String, Object> condition);

    User select(String id);

    int insert(User user);

    int batchInsert(List<User> users);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(User user);

    int batchUpdate(List<User> users);
}
