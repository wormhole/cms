package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.dao.*;
import net.stackoverflow.cms.model.entity.*;
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
    public List<User> selectByPage(Page page) {
        return userDAO.selectByPage(page);
    }

    @Override
    public List<User> selectByCondition(Map<String, Object> searchMap) {
        return userDAO.selectByCondition(searchMap);
    }

    @Override
    public User select(String id) {
        return userDAO.select(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(User user) {
        return userDAO.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<User> users) {
        return userDAO.batchInsert(users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(String id) {
        List<UserRole> userRoles = userRoleDAO.selectByCondition(new HashMap<String, Object>(16) {{
            put("userId", id);
        }});
        if (userRoles != null && userRoles.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (UserRole userRole : userRoles) {
                ids.add(userRole.getId());
            }
            userRoleDAO.batchDelete(ids);
        }
        return userDAO.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<String> ids) {

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
        userRoleDAO.batchDelete(userRoleIds);
        return userDAO.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(User user) {
        return userDAO.update(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<User> users) {
        return userDAO.batchUpdate(users);
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
        userRoleDAO.batchInsert(userRoles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokeRole(String userId, String roleId) {
        List<UserRole> userRoles = userRoleDAO.selectByCondition(new HashMap<String, Object>(16) {{
            put("userId", userId);
            put("roleId", roleId);
        }});

        if (userRoles.size() == 0) {
            return;
        }

        userRoleDAO.delete(userRoles.get(0).getId());
    }

    @Override
    public List<Role> getRoleByUserId(String userId) {
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
    public List<Permission> getPermissionByUserId(String userId) {
        List<Role> roles = getRoleByUserId(userId);
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
}
