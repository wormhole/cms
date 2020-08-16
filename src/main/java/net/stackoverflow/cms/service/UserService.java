package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.model.dto.GrantRoleDTO;
import net.stackoverflow.cms.model.dto.PermissionDTO;
import net.stackoverflow.cms.model.dto.RoleDTO;
import net.stackoverflow.cms.model.dto.UserDTO;
import net.stackoverflow.cms.model.entity.User;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author 凉衫薄
 */
public interface UserService {

    User findById(String id);

    User findByUsername(String username);

    List<RoleDTO> findRoleByUserId(String userId);

    List<PermissionDTO> findPermissionByUserId(String userId);

    void save(UserDTO dto);

    void updateBase(UserDTO dto);

    void updatePassword(String id, String oldPassword, String newPassword);

    void updatePassword(String id, String password);

    PageResponse<UserDTO> findByPage(Integer page, Integer limit, String sort, String order, String key, List<String> roleIds);

    void deleteByIds(List<String> ids);

    void updateEnable(List<String> ids, Integer enable);

    void reGrandRole(GrantRoleDTO dto);

    Integer count();
}
