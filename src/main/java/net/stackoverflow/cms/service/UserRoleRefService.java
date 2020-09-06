package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.entity.UserRoleRef;

import java.util.List;

/**
 * 用户角色关联服务接口
 *
 * @author 凉衫薄
 */
public interface UserRoleRefService {

    /**
     * 通过UserId查询关联关系
     *
     * @param userId 用户主键
     * @return
     */
    List<UserRoleRef> findByUserId(String userId);

    /**
     * 通过角色主键列表查询关联关系
     *
     * @param roleIds 角色主键列表
     * @return
     */
    List<UserRoleRef> findByRoleIds(List<String> roleIds);

    /**
     * 根据用户主键列表删除关联关系
     *
     * @param userIds 用户主键列表
     */
    void deleteByUserIds(List<String> userIds);

    /**
     * 根据用户主键删除关联关系
     *
     * @param userId 用户主键
     */
    void deleteByUserId(String userId);

    /**
     * 根据角色主键删除关联关系
     *
     * @param roleIds
     */
    void deleteByRoleIds(List<String> roleIds);

    /**
     * 批量新增
     *
     * @param userRoleRefs
     */
    void batchSave(List<UserRoleRef> userRoleRefs);
}
