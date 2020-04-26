package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> findByPage(Page page);

    List<User> findByCondition(Map<String, Object> condition);

    List<User> findByIds(List<String> ids);

    User findByUsername(String username);

    User findById(String id);

    Integer count();

    void save(User user);

    void batchDelete(List<String> ids);

    void update(User user);

    void batchUpdate(List<User> users);

    void grantRole(String userId, String roleId);

    void reGrantRole(String userId, List<String> roleIds);

    List<Role> findRoleByUserId(String userId);

    List<Permission> findPermissionByUserId(String userId);

    void saveWithRole(User user, String role);
}
