package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.model.dto.BindRoleDTO;
import net.stackoverflow.cms.model.dto.UserDTO;
import net.stackoverflow.cms.model.entity.User;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author 凉衫薄
 */
public interface UserService {

    /**
     * 根据用户主键查询
     *
     * @param id 用户主键
     * @return
     */
    User findById(String id);

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return
     */
    User findByUsername(String username);

    /**
     * 保存用户信息
     *
     * @param dto
     */
    void save(UserDTO dto);

    /**
     * 更新用户基本信息
     *
     * @param dto
     */
    void updateBase(UserDTO dto);

    /**
     * 更新密码
     *
     * @param id          用户主键
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updatePassword(String id, String oldPassword, String newPassword);

    /**
     * 更新密码
     *
     * @param id       用户主键
     * @param password 新密码
     */
    void updatePassword(String id, String password);

    /**
     * 分页查询用户信息
     *
     * @param page    当前页
     * @param limit   每页大小
     * @param sort    排序字段
     * @param order   排序方式
     * @param key     关键字
     * @param roleIds 角色主键过滤
     * @return
     */
    PageResponse<UserDTO> findByPage(Integer page, Integer limit, String sort, String order, String key, List<String> roleIds);

    /**
     * 根据用户主键删除
     *
     * @param ids 用户主键列表
     */
    void deleteByIds(List<String> ids);

    /**
     * 更新用户启用条件
     *
     * @param ids    用户主键列表
     * @param enable 启用或停用
     */
    void updateEnable(List<String> ids, Integer enable);

    /**
     * 重新分配用户角色
     *
     * @param dto
     */
    void reGrandRole(BindRoleDTO dto);

    /**
     * 统计用户数量
     *
     * @return
     */
    Integer count();
}
