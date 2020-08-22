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

    /**
     * 查询所有权限
     *
     * @return
     */
    List<PermissionDTO> findAll();

    /**
     * 分页查询权限
     *
     * @param page  当前页
     * @param limit 每页大小
     * @param sort  排序字段
     * @param order 排序方式
     * @param key   关键字
     * @return
     */
    PageResponse<PermissionDTO> findByPage(Integer page, Integer limit, String sort, String order, String key);

    /**
     * 根据主键列表删除权限
     *
     * @param ids 主键列表
     */
    void deleteByIds(List<String> ids);

    /**
     * 更新权限基本信息
     *
     * @param permissionDTO
     */
    void update(PermissionDTO permissionDTO);

    /**
     * 保存权限
     *
     * @param permissionDTO
     */
    void save(PermissionDTO permissionDTO);

    /**
     * 统计数量
     *
     * @return
     */
    Integer count();

    /**
     * 根据用户主键查询权限
     *
     * @param userId 用户主键
     * @return
     */
    List<PermissionDTO> findByUserId(String userId);

    /**
     * 根据角色主键查询权限
     *
     * @param roleId 角色主键
     * @return
     */
    List<PermissionDTO> findByRoleId(String roleId);
}
