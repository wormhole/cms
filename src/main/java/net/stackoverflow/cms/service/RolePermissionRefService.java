package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.entity.RolePermissionRef;

import java.util.List;

/**
 * 角色权限关联服务接口
 *
 * @author 凉衫薄
 */
public interface RolePermissionRefService {

    List<RolePermissionRef> findByRoleId(String roleId);

    List<RolePermissionRef> findByRoleIds(List<String> roleIds);

    List<RolePermissionRef> findByPermissionIds(List<String> permissionIds);

    void deleteByRoleId(String roleId);

    void batchSave(List<RolePermissionRef> rolePermissionRefs);
}
