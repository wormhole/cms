package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;

import java.util.List;
import java.util.Map;

public interface RoleService {

    List<Role> findByPage(Page page);

    List<Role> findByCondition(Map<String, Object> condition);

    List<Role> findAll();

    Role findById(String id);

    List<Role> findByIds(List<String> ids);

    void save(Role role);

    void batchDelete(List<String> ids);

    void update(Role role);

    void reGrantPermission(String roleId, List<String> permissionIds);

    List<Permission> findPermissionByRoleId(String roleId);

    List<User> findUserByRoleIds(List<String> roleIds);
}
