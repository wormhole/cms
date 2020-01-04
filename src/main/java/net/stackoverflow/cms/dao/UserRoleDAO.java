package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserRoleDAO {

    List<UserRole> selectByPage(Page page);

    List<UserRole> selectByCondition(Map<String, Object> condition);

    UserRole select(String id);

    int insert(UserRole userRole);

    int batchInsert(List<UserRole> userRoles);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(UserRole userRole);

    int batchUpdate(List<UserRole> userRoles);

}
