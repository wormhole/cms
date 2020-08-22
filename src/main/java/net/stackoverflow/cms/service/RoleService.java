package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.model.dto.RoleDTO;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author 凉衫薄
 */
public interface RoleService {

    List<RoleDTO> findAll();

    PageResponse<RoleDTO> findByPage(Integer page, Integer limit, String sort, String order, String key, List<String> permissionIds);

    void deleteByIds(List<String> ids);

    void reGrantPermission(String roleId, List<String> permissionIds);

    void update(RoleDTO roleDTO);

    void save(RoleDTO roleDTO);

    Integer count();

    List<RoleDTO> findByUserId(String userId);
}
