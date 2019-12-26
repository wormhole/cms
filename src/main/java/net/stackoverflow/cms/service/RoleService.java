package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;

import java.util.List;
import java.util.Map;

public interface RoleService {

    List<Role> selectByPage(Page page);

    List<Role> selectByCondition(Map<String, Object> searchMap);

    Role select(String id);

    int insert(Role role);

    int batchInsert(List<Role> roles);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(Role role);

    int batchUpdate(List<Role> roles);

    void grantPermission(String roleId, String permissionId);

    void reGrantPermission(String roleId, List<String> permissionIds);

    void revokePermission(String roleId, String permissionId);

    List<Permission> getPermissionByRoleId(String roleId);

    List<User> selectUserByRoleIds(List<String> roleIds);
}
