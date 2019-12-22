package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> selectByPage(Page page);

    List<User> selectByCondition(Map<String, Object> searchMap);

    User select(String id);

    int insert(User user);

    int batchInsert(List<User> users);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(User user);

    int batchUpdate(List<User> users);

    void grantRole(String userId, String roleId);

    void reGrantRole(String userId, List<String> roleIds);

    void revokeRole(String userId, String roleIds);

    List<Role> getRoleByUserId(String userId);

    List<Permission> getPermissionByUserId(String userId);
}
