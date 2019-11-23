package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.dao.PermissionDAO;
import net.stackoverflow.cms.dao.RoleDAO;
import net.stackoverflow.cms.dao.RolePermissionDAO;
import net.stackoverflow.cms.pojo.entity.Permission;
import net.stackoverflow.cms.pojo.entity.Role;
import net.stackoverflow.cms.pojo.entity.RolePermission;
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

    @Override
    public List<Role> selectByPage(Page page) {
        return roleDAO.selectByPage(page);
    }

    @Override
    public List<Role> selectByCondition(Map<String, Object> searchMap) {
        return roleDAO.selectByCondition(searchMap);
    }

    @Override
    public Role select(String id) {
        return roleDAO.select(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Role role) {
        return roleDAO.insert(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<Role> roles) {
        return roleDAO.batchInsert(roles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(String id) {
        return roleDAO.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<String> ids) {
        return roleDAO.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Role role) {
        return roleDAO.update(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<Role> roles) {
        return roleDAO.batchUpdate(roles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantPermission(String roleId, String permissionId) {
        Role role = roleDAO.select(roleId);
        Permission permission = permissionDAO.select(permissionId);

        if (role == null || permission == null) {
            return;
        }

        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(UUID.randomUUID().toString());
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionId(permissionId);
        rolePermissionDAO.insert(rolePermission);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokePermission(String roleId, String permissionId) {
        List<RolePermission> rolePermissions = rolePermissionDAO.selectByCondition(new HashMap<String, Object>(16) {{
            put("roleId", roleId);
            put("permissionId", permissionId);
        }});

        if (rolePermissions.size() == 0) {
            return;
        }

        rolePermissionDAO.delete(rolePermissions.get(0).getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Permission> getPermissionByRoleId(String roleId) {
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
}
