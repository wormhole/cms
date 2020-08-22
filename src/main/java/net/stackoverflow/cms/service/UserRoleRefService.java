package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.entity.UserRoleRef;

import java.util.List;

/**
 * 用户角色关联服务接口
 *
 * @author 凉衫薄
 */
public interface UserRoleRefService {

    List<UserRoleRef> findByUserId(String userId);

    List<UserRoleRef> findByRoleIds(List<String> roleIds);

    void deleteByUserIds(List<String> userIds);

    void deleteByUserId(String userId);

    void batchSave(List<UserRoleRef> userRoleRefs);
}
