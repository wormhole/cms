package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.common.QueryWrapper.QueryWrapperBuilder;
import net.stackoverflow.cms.dao.*;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.dto.GrantRoleDTO;
import net.stackoverflow.cms.model.dto.PermissionDTO;
import net.stackoverflow.cms.model.dto.RoleDTO;
import net.stackoverflow.cms.model.dto.UserDTO;
import net.stackoverflow.cms.model.entity.*;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 用户服务实现类
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private PermissionDAO permissionDAO;
    @Autowired
    private UserRoleRefDAO userRoleRefDAO;
    @Autowired
    private RolePermissionRefDAO rolePermissionRefDAO;

    @Override
    public User findById(String id) {
        User user = null;
        if (id != null) {
            user = userDAO.select(id);
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = null;
        if (!StringUtils.isEmpty(username)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.eq("username", username);
            List<User> users = userDAO.selectByCondition(builder.build());
            if (!CollectionUtils.isEmpty(users)) {
                user = users.get(0);
            }
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RoleDTO> findRoleByUserId(String userId) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.eq("userId", userId);
        List<UserRoleRef> userRoleRefs = userRoleRefDAO.selectByCondition(builder.build());
        List<RoleDTO> roleDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoleRefs)) {
            List<String> roleIds = new ArrayList<>();
            userRoleRefs.forEach(userRoleRef -> {
                roleIds.add(userRoleRef.getRoleId());

            });
            List<Role> roles = roleDAO.selectByCondition(QueryWrapper.newBuilder().in("id", roleIds.toArray()).build());
            roles.forEach(role -> {
                RoleDTO dto = new RoleDTO();
                BeanUtils.copyProperties(role, dto);
                roleDTOS.add(dto);
            });
        }
        return roleDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PermissionDTO> findPermissionByUserId(String userId) {
        List<RoleDTO> roleDTOS = findRoleByUserId(userId);

        List<PermissionDTO> permissionDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleDTOS)) {
            Set<String> roleIds = new HashSet<>();
            roleDTOS.forEach(roleDTO -> roleIds.add(roleDTO.getId()));

            QueryWrapperBuilder builder = QueryWrapper.newBuilder();
            builder.in("roleId", roleIds.toArray());
            List<RolePermissionRef> rolePermissionRefs = rolePermissionRefDAO.selectByCondition(builder.build());
            if (!CollectionUtils.isEmpty(rolePermissionRefs)) {
                Set<String> permissionIds = new HashSet<>();
                rolePermissionRefs.forEach(rolePermissionRef -> permissionIds.add(rolePermissionRef.getPermissionId()));

                List<Permission> permissions = permissionDAO.selectByCondition(QueryWrapper.newBuilder().in("id", permissionIds.toArray()).build());
                permissions.forEach(permission -> {
                    PermissionDTO permissionDTO = new PermissionDTO();
                    BeanUtils.copyProperties(permission, permissionDTO);
                    permissionDTOS.add(permissionDTO);
                });
            }
        }

        return permissionDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserDTO dto) {
        //校验数据
        User u = findByUsername(dto.getUsername());
        if (u != null) {
            throw new BusinessException("用户名重复");
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setId(UUID.randomUUID().toString());
        user.setBuiltin(0);
        user.setTs(new Date());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userDAO.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBase(UserDTO dto) {

        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.neq("id", dto.getId());
        builder.eq("username", dto.getUsername());
        List<User> users = userDAO.selectByCondition(builder.build());

        if (!CollectionUtils.isEmpty(users)) {
            throw new BusinessException("用户名不能重复");
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setTs(new Date());
        userDAO.update(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String id, String oldPassword, String newPassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userDAO.select(id);
        if (!encoder.encode(oldPassword).equals(user.getPassword())) {
            throw new BusinessException("旧密码不匹配");
        }

        String password = encoder.encode(newPassword);

        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.update("password", password);
        builder.update("ts", new Date());
        builder.eq("id", user.getId());
        userDAO.updateByCondition(builder.build());
    }

    @Override
    @Transactional
    public PageResponse<UserDTO> findByPage(Integer page, Integer limit, String sort, String order, String key, List<String> roleIds) {
        Set<String> userIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(roleIds)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.in("roleId", roleIds.toArray());
            List<UserRoleRef> userRoleRefs = userRoleRefDAO.selectByCondition(builder.build());
            if (!CollectionUtils.isEmpty(userRoleRefs)) {
                userRoleRefs.forEach(userRoleRef -> userIds.add(userRoleRef.getUserId()));
            } else {
                return new PageResponse<>(0, new ArrayList<>());
            }
        }

        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.sort("builtin", "desc");
        if (StringUtils.isEmpty(sort) || StringUtils.isEmpty(order)) {
            builder.sort("username", "asc");
        } else {
            builder.sort(sort, order);
        }
        builder.like(!StringUtils.isEmpty(key), key, Arrays.asList("username", "telephone", "email"));
        builder.page((page - 1) * limit, limit);
        builder.in("id", userIds.toArray());
        QueryWrapper wrapper = builder.build();

        List<User> users = userDAO.selectByCondition(wrapper);
        Integer total = userDAO.countByCondition(wrapper);

        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setPassword(null);
            List<RoleDTO> roleDTOS = findRoleByUserId(user.getId());
            userDTO.setRoles(roleDTOS);
            userDTOS.add(userDTO);
        }
        return new PageResponse<>(total, userDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.in("id", ids.toArray());
        List<User> users = userDAO.selectByCondition(builder.build());
        for (User user : users) {
            if (user.getBuiltin().equals(1)) {
                throw new BusinessException("内建用户不允许被删除");
            }
        }

        userDAO.batchDelete(ids);
        userRoleRefDAO.deleteByCondition(new QueryWrapperBuilder().in("userId", ids.toArray()).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnable(List<String> ids, Integer enable) {
        List<User> users = userDAO.selectByCondition(new QueryWrapperBuilder().in("id", ids.toArray()).build());
        for (User user : users) {
            if (user.getBuiltin().equals(1))
                throw new BusinessException("超级管理员不允许被操作");
        }

        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.update("enable", enable);
        builder.update("ts", new Date());
        builder.in("id", ids.toArray());
        userDAO.updateByCondition(builder.build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reGrandRole(GrantRoleDTO dto) {
        userRoleRefDAO.deleteByCondition(QueryWrapper.newBuilder().eq("userId", dto.getUserId()).build());
        List<UserRoleRef> userRoleRefs = new ArrayList<>();
        for (String roleId : dto.getRoleIds()) {
            userRoleRefs.add(new UserRoleRef(UUID.randomUUID().toString(), dto.getUserId(), roleId, new Date()));
        }
        userRoleRefDAO.batchInsert(userRoleRefs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer count() {
        return userDAO.countByCondition(new QueryWrapper());
    }

}
