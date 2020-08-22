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

    /**
     * 查询所有角色
     *
     * @return
     */
    List<RoleDTO> findAll();

    /**
     * 分页查询角色
     *
     * @param page          当前页
     * @param limit         每页大小
     * @param sort          排序字段
     * @param order         排序方式
     * @param key           关键字
     * @param permissionIds 权限主键过滤
     * @return
     */
    PageResponse<RoleDTO> findByPage(Integer page, Integer limit, String sort, String order, String key, List<String> permissionIds);

    /**
     * 根据主键列表删除角色
     *
     * @param ids
     */
    void deleteByIds(List<String> ids);

    /**
     * 重新给角色分配权限
     *
     * @param roleId        角色主键
     * @param permissionIds 权限主键列表
     */
    void reGrantPermission(String roleId, List<String> permissionIds);

    /**
     * 更新角色信息
     *
     * @param roleDTO
     */
    void update(RoleDTO roleDTO);

    /**
     * 保存角色信息
     *
     * @param roleDTO
     */
    void save(RoleDTO roleDTO);

    /**
     * 统计角色数量
     *
     * @return
     */
    Integer count();

    /**
     * 根据用户主键查询角色信息
     *
     * @param userId 用户主键
     * @return
     */
    List<RoleDTO> findByUserId(String userId);
}
