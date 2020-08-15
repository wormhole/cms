package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.model.dto.PermissionDTO;

import java.util.List;

/**
 * 权限服务接口
 *
 * @author 凉衫薄
 */
public interface PermissionService {

    List<PermissionDTO> findAll();

    PageResponse<PermissionDTO> findByPage(Integer page, Integer limit, String sort, String order, String key);

    void deleteByIds(List<String> ids);

    void update(PermissionDTO permissionDTO);

    void save(PermissionDTO permissionDTO);

    Integer count();
}
