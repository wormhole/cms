package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;

import java.util.List;
import java.util.Map;

public interface PermissionService {

    List<Permission> findByPage(Page page);

    List<Permission> findByCondition(Map<String, Object> condition);

    Permission findByName(String name);

    List<Permission> findAll();

    Permission findById(String id);

    List<Permission> findByIds(List<String> ids);

    void save(Permission permission);

    void batchDelete(List<String> ids);

    void update(Permission permission);

    List<Role> findRoleByPermissionIds(List<String> permissionIds);
}
