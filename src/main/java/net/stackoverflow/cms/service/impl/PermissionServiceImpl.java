package net.stackoverflow.cms.service.impl;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.dao.PermissionDAO;
import net.stackoverflow.cms.dao.RoleDAO;
import net.stackoverflow.cms.dao.RolePermissionDAO;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.RolePermission;
import net.stackoverflow.cms.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDAO permissionDAO;
    @Autowired
    private RolePermissionDAO rolePermissionDAO;
    @Autowired
    private RoleDAO roleDAO;

    @Override
    public List<Permission> findByPage(Page page) {
        return permissionDAO.selectByPage(page);
    }

    @Override
    public List<Permission> findByCondition(Map<String, Object> condition) {
        return permissionDAO.selectByCondition(condition);
    }

    @Override
    public Permission findByName(String name) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("name", name);
        List<Permission> permissions = permissionDAO.selectByCondition(condition);
        if (permissions != null && permissions.size() > 0) {
            return permissions.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Permission> findAll() {
        return permissionDAO.selectByCondition(new HashMap<>());
    }

    @Override
    public Permission findById(String id) {
        return permissionDAO.select(id);
    }

    @Override
    public List<Permission> findByIds(List<String> ids) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("ids", ids);
        return permissionDAO.selectByCondition(condition);
    }

    @Override
    public Integer count() {
        return permissionDAO.selectByCondition(new HashMap<>()).size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Permission permission) {
        permissionDAO.insert(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        permissionDAO.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Permission permission) {
        permissionDAO.update(permission);
    }

    @Override
    public List<Role> findRoleByPermissionIds(List<String> permissionIds) {
        Set<String> roleIds = new HashSet<>();
        for (String permissionId : permissionIds) {
            List<RolePermission> rolePermissions = rolePermissionDAO.selectByCondition(new HashMap<String, Object>(16) {{
                put("permissionId", permissionId);
            }});
            if (rolePermissions != null && rolePermissions.size() > 0) {
                for (RolePermission rolePermission : rolePermissions) {
                    roleIds.add(rolePermission.getRoleId());
                }
            }
        }
        if (roleIds.size() > 0) {
            return roleDAO.selectByCondition(new HashMap<String, Object>(16) {{
                put("ids", roleIds);
            }});
        } else {
            return null;
        }
    }
}
