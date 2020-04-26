package net.stackoverflow.cms.service.impl;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.dao.*;
import net.stackoverflow.cms.model.entity.*;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private PermissionDAO permissionDAO;
    @Autowired
    private UserRoleDAO userRoleDAO;
    @Autowired
    private RolePermissionDAO rolePermissionDAO;

    @Override
    public List<User> findByPage(Page page) {
        return userDAO.selectByPage(page);
    }

    @Override
    public List<User> findByCondition(Map<String, Object> condition) {
        return userDAO.selectByCondition(condition);
    }

    @Override
    public List<User> findByIds(List<String> ids) {
        Map<String, Object> condition = new HashMap<>(16);
        condition.put("ids", ids);
        return userDAO.selectByCondition(condition);
    }

    @Override
    public User findByUsername(String username) {
        Map<String, Object> condition = new HashMap<>(16);
        condition.put("username", username);
        List<User> users = userDAO.selectByCondition(condition);
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User findById(String id) {
        return userDAO.select(id);
    }

    @Override
    public Integer count() {
        return userDAO.selectByCondition(new HashMap<>()).size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        userDAO.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {

        List<String> userRoleIds = new ArrayList<>();
        for (String id : ids) {
            List<UserRole> userRoles = userRoleDAO.selectByCondition(new HashMap<String, Object>(16) {{
                put("userId", id);
            }});
            if (userRoles != null && userRoles.size() > 0) {
                for (UserRole userRole : userRoles) {
                    userRoleIds.add(userRole.getId());
                }
            }
        }
        if (userRoleIds.size() > 0) {
            userRoleDAO.batchDelete(userRoleIds);
        }
        userDAO.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<User> users) {
        userDAO.batchUpdate(users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantRole(String userId, String roleId) {
        User user = userDAO.select(userId);
        Role role = roleDAO.select(roleId);

        if (user == null || role == null) {
            return;
        }

        UserRole userRole = new UserRole();
        userRole.setId(UUID.randomUUID().toString());
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);
        userRoleDAO.insert(userRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reGrantRole(String userId, List<String> roleIds) {
        List<UserRole> userRoles = userRoleDAO.selectByCondition(new HashMap<String, Object>(16) {{
            put("userId", userId);
        }});
        if (userRoles != null && userRoles.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (UserRole userRole : userRoles) {
                ids.add(userRole.getId());
            }
            userRoleDAO.batchDelete(ids);
        }
        userRoles = new ArrayList<>();
        for (String roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setId(UUID.randomUUID().toString());
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        }
        if (userRoles.size() > 0) {
            userRoleDAO.batchInsert(userRoles);
        }
    }

    @Override
    public List<Role> findRoleByUserId(String userId) {
        List<UserRole> userRoles = userRoleDAO.selectByCondition(new HashMap<String, Object>(16) {{
            put("userId", userId);
        }});
        List<Role> roles = new ArrayList<>();
        if ((null != userRoles) && (userRoles.size() > 0)) {
            for (UserRole userRole : userRoles) {
                Role role = roleDAO.select(userRole.getRoleId());
                roles.add(role);
            }
        }
        return roles;
    }

    @Override
    public List<Permission> findPermissionByUserId(String userId) {
        List<Role> roles = findRoleByUserId(userId);
        Map<String, Permission> permissionMap = new HashMap<>(16);
        List<Permission> permissions = new ArrayList<>();
        for (Role role : roles) {
            List<RolePermission> rolePermissions = rolePermissionDAO.selectByCondition(new HashMap<String, Object>(16) {{
                put("roleId", role.getId());
            }});
            if ((null != rolePermissions) && (rolePermissions.size() > 0)) {
                for (RolePermission rolePermission : rolePermissions) {
                    Permission permission = permissionDAO.select(rolePermission.getPermissionId());
                    permissionMap.put(permission.getId(), permission);
                }
            }
        }
        if (permissionMap.size() > 0) {
            permissions = new ArrayList<>(permissionMap.values());
        }
        return permissions;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithRole(User user, String role) {
        userDAO.insert(user);
        List<Role> roles = roleDAO.selectByCondition(new HashMap<String, Object>(16) {{
            put("name", role);
        }});
        if (roles != null && roles.size() == 1) {
            Role guest = roles.get(0);
            grantRole(user.getId(), guest.getId());
        }
    }
}
