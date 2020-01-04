package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleDAO {

    List<Role> selectByPage(Page page);

    List<Role> selectByCondition(Map<String, Object> condition);

    Role select(String id);

    int insert(Role role);

    int batchInsert(List<Role> roles);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(Role role);

    int batchUpdate(List<Role> roles);

}
