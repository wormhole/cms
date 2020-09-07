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
     * @param page  当前页
     * @param limit 每页大小
     * @param sort  排序字段
     * @param order 排序方式
     * @param key   关键字
     * @return
     */
    PageResponse<RoleDTO> findByPage(Integer page, Integer limit, String sort, String order, String key);

    /**
     * 根据主键列表删除角色
     *
     * @param ids 用户主键列表
     */
    void deleteByIds(List<String> ids);

    /**
     * 更新角色信息
     *
     * @param dto 角色信息dto对象
     */
    void update(RoleDTO dto);

    /**
     * 保存角色信息
     *
     * @param dto 角色信息dto对象
     */
    void save(RoleDTO dto);

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

    /**
     * 根据用户主键查询角色key
     *
     * @param userId 用户主键
     * @return
     */
    List<String> findNamesByUserId(String userId);

    /**
     * 根据角色主键查询角色信息
     *
     * @param id 角色主键
     * @return
     */
    RoleDTO findById(String id);

    /**
     * 根据角色名查找id
     *
     * @param name 角色名
     * @return
     */
    String findIdByName(String name);
}
