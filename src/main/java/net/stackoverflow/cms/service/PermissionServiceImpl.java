package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.dao.PermissionDAO;
import net.stackoverflow.cms.dao.RoleDAO;
import net.stackoverflow.cms.dao.RolePermissionDAO;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.RolePermission;
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
    public List<Permission> selectByPage(Page page) {
        return permissionDAO.selectByPage(page);
    }

    @Override
    public List<Permission> selectByCondition(Map<String, Object> searchMap) {
        return permissionDAO.selectByCondition(searchMap);
    }

    @Override
    public Permission select(String id) {
        return permissionDAO.select(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Permission permission) {
        return permissionDAO.insert(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Permission> permissions) {
        return permissionDAO.batchInsert(permissions);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(String id) {
        return permissionDAO.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<String> ids) {
        return permissionDAO.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Permission permission) {
        return permissionDAO.update(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Permission> permissions) {
        return permissionDAO.batchUpdate(permissions);
    }

    @Override
    public List<Role> selectRoleByPermissionIds(List<String> permissionIds) {
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
