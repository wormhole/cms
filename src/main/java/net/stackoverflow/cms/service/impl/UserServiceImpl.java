package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.common.QueryWrapper.QueryWrapperBuilder;
import net.stackoverflow.cms.dao.UserDAO;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.dto.BindRoleDTO;
import net.stackoverflow.cms.model.dto.RoleDTO;
import net.stackoverflow.cms.model.dto.UserDTO;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.entity.UserRoleRef;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.service.UserRoleRefService;
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
    private RoleService roleService;
    @Autowired
    private UserRoleRefService userRoleRefService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User findById(String id) {
        User user = null;
        if (id != null) {
            user = userDAO.select(id);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User findByUsername(String username) {
        User user = null;
        if (!StringUtils.isEmpty(username)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.eq("username", username);
            List<User> users = userDAO.querySelect(builder.build());
            if (!CollectionUtils.isEmpty(users)) {
                user = users.get(0);
            }
        }
        return user;
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
        user.setEnable(1);
        user.setTs(new Date());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userDAO.insert(user);

        Role role = roleService.findByName("guest");
        if (role != null) {
            userRoleRefService.save(new UserRoleRef(UUID.randomUUID().toString(), user.getId(), role.getId(), new Date()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBase(UserDTO dto) {

        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.neq("id", dto.getId());
        builder.eq("username", dto.getUsername());
        List<User> users = userDAO.querySelect(builder.build());

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
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码不匹配");
        }

        String password = encoder.encode(newPassword);

        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.update("password", password);
        builder.update("ts", new Date());
        builder.eq("id", user.getId());
        userDAO.queryUpdate(builder.build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String id, String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userDAO.select(id);
        password = encoder.encode(password);

        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.update("password", password);
        builder.update("ts", new Date());
        builder.eq("id", user.getId());
        userDAO.queryUpdate(builder.build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResponse<UserDTO> findByPage(Integer page, Integer limit, String sort, String order, String key, List<String> roleIds) {
        Set<String> userIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<UserRoleRef> userRoleRefs = userRoleRefService.findByRoleIds(roleIds);
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
        builder.in("id", new ArrayList<>(userIds));
        QueryWrapper wrapper = builder.build();

        List<User> users = userDAO.querySelect(wrapper);
        Integer total = userDAO.queryCount(wrapper);

        List<UserDTO> dtos = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setPassword(null);
            List<RoleDTO> roles = roleService.findByUserId(user.getId());
            userDTO.setRoles(roles);
            dtos.add(userDTO);
        }
        return new PageResponse<>(total, dtos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.in("id", ids);
            List<User> users = userDAO.querySelect(builder.build());
            for (User user : users) {
                if (user.getBuiltin().equals(1)) {
                    throw new BusinessException("内建用户不允许被删除");
                }
            }

            userDAO.batchDelete(ids);
            userRoleRefService.deleteByUserIds(ids);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnable(List<String> ids, Integer enable) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<User> users = userDAO.querySelect(new QueryWrapperBuilder().in("id", ids).build());
            for (User user : users) {
                if (user.getBuiltin().equals(1)) {
                    throw new BusinessException("超级管理员不允许被操作");
                }
            }

            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.update("enable", enable);
            builder.update("ts", new Date());
            builder.in("id", ids);
            userDAO.queryUpdate(builder.build());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reGrandRole(BindRoleDTO dto) {
        String userId = dto.getUserId();
        List<String> roleIds = dto.getRoleIds();

        userRoleRefService.deleteByUserId(userId);
        List<UserRoleRef> userRoleRefs = new ArrayList<>();
        for (String roleId : roleIds) {
            userRoleRefs.add(new UserRoleRef(UUID.randomUUID().toString(), userId, roleId, new Date()));
        }
        if (!CollectionUtils.isEmpty(userRoleRefs)) {
            userRoleRefService.batchSave(userRoleRefs);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer count() {
        return userDAO.queryCount(new QueryWrapper());
    }

}
