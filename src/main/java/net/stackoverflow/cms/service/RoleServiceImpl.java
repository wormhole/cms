package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.dao.*;
import net.stackoverflow.cms.model.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private PermissionDAO permissionDAO;
    @Autowired
    private RolePermissionDAO rolePermissionDAO;
    @Autowired
    private UserRoleDAO userRoleDAO;
    @Autowired
    private UserDAO userDAO;

    @Override
    public List<Role> findByPage(Page page) {
        return roleDAO.selectByPage(page);
    }

    @Override
    public List<Role> findByCondition(Map<String, Object> searchMap) {
        return roleDAO.selectByCondition(searchMap);
    }

    @Override
    public List<Role> findAll() {
        return roleDAO.selectByCondition(new HashMap<>());
    }

    @Override
    public Role findById(String id) {
        return roleDAO.select(id);
    }

    @Override
    public List<Role> findByIds(List<String> ids) {
        Map<String, Object> searchMap = new HashMap<>(16);
        searchMap.put("ids", ids);
        return roleDAO.selectByCondition(searchMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Role role) {
        roleDAO.insert(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        roleDAO.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Role role) {
        roleDAO.update(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reGrantPermission(String roleId, List<String> permissionIds) {
        List<RolePermission> rolePermissions = rolePermissionDAO.selectByCondition(new HashMap<String, Object>(16) {{
            put("roleId", roleId);
        }});
        if (rolePermissions != null && rolePermissions.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (RolePermission rolePermission : rolePermissions) {
                ids.add(rolePermission.getId());
            }
            rolePermissionDAO.batchDelete(ids);
        }
        rolePermissions = new ArrayList<>();
        for (String permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setId(UUID.randomUUID().toString());
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissions.add(rolePermission);
        }
        if (rolePermissions.size() > 0) {
            rolePermissionDAO.batchInsert(rolePermissions);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Permission> findPermissionByRoleId(String roleId) {
        List<Permission> permissions = new ArrayList<>();

        List<RolePermission> rolePermissions = rolePermissionDAO.selectByCondition(new HashMap<String, Object>(16) {{
            put("roleId", roleId);
        }});
        if ((null != rolePermissions) && (rolePermissions.size() > 0)) {
            for (RolePermission rolePermission : rolePermissions) {
                Permission permission = permissionDAO.select(rolePermission.getPermissionId());
                permissions.add(permission);
            }
        }

        return permissions;
    }

    @Override
    public List<User> findUserByRoleIds(List<String> roleIds) {

        Set<String> userIds = new HashSet<>();
        for (String roleId : roleIds) {
            List<UserRole> userRoles = userRoleDAO.selectByCondition(new HashMap<String, Object>(16) {{
                put("roleId", roleId);
            }});
            if (userRoles != null && userRoles.size() > 0) {
                for (UserRole userRole : userRoles) {
                    userIds.add(userRole.getUserId());
                }
            }
        }
        if (userIds.size() > 0) {
            return userDAO.selectByCondition(new HashMap<String, Object>(16) {{
                put("ids", userIds);
            }});
        } else {
            return null;
        }
    }
}
