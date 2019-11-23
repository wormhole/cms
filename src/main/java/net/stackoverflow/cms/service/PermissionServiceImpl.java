package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.dao.PermissionDAO;
import net.stackoverflow.cms.pojo.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDAO permissionDAO;

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
}
