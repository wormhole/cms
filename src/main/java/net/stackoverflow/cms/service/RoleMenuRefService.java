package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.entity.RoleMenuRef;

import java.util.List;

/**
 * 角色菜单关联服务接口
 *
 * @author 凉衫薄
 */
public interface RoleMenuRefService {

    /**
     * 根据角色主键列表删除
     *
     * @param roleIds
     */
    void deleteByRoleIds(List<String> roleIds);

    /**
     * 根据角色Id删除
     *
     * @param roleId
     */
    void deleteByRoleId(String roleId);

    /**
     * 保存关联关系
     *
     * @param refs
     */
    void batchSave(List<RoleMenuRef> refs);

    /**
     * 根据角色id查找关联关系
     *
     * @param id
     * @return
     */
    List<RoleMenuRef> findByRoleId(String id);
}
