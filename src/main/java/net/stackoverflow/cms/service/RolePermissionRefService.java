package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.entity.RolePermissionRef;

import java.util.List;

/**
 * 角色权限关联服务接口
 *
 * @author 凉衫薄
 */
public interface RolePermissionRefService {

    /**
     * 根据角色主键查询关联关系
     *
     * @param roleId 角色主键
     * @return
     */
    List<RolePermissionRef> findByRoleId(String roleId);

    /**
     * 根据角色主键列表查询关联关系
     *
     * @param roleIds 角色主键列表
     * @return
     */
    List<RolePermissionRef> findByRoleIds(List<String> roleIds);

    /**
     * 根据权限主角列表查询关联关系
     *
     * @param permissionIds 权限主键列表
     * @return
     */
    List<RolePermissionRef> findByPermissionIds(List<String> permissionIds);

    /**
     * 根据角色主键删除关联关系
     *
     * @param roleId 角色主键
     */
    void deleteByRoleId(String roleId);

    /**
     * 批量新增关联关系
     *
     * @param rolePermissionRefs
     */
    void batchSave(List<RolePermissionRef> rolePermissionRefs);
}
