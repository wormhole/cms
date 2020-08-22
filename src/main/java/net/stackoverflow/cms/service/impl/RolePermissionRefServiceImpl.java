package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.common.QueryWrapper.QueryWrapperBuilder;
import net.stackoverflow.cms.dao.RolePermissionRefDAO;
import net.stackoverflow.cms.model.entity.RolePermissionRef;
import net.stackoverflow.cms.service.RolePermissionRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色权限关联服务实现类
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class RolePermissionRefServiceImpl implements RolePermissionRefService {

    @Autowired
    private RolePermissionRefDAO rolePermissionRefDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RolePermissionRef> findByRoleId(String roleId) {
        List<RolePermissionRef> rolePermissionRefs = new ArrayList<>();
        if (!StringUtils.isEmpty(roleId)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.eq("role_id", roleId);
            rolePermissionRefs = rolePermissionRefDAO.selectByCondition(builder.build());
        }
        return rolePermissionRefs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RolePermissionRef> findByRoleIds(List<String> roleIds) {
        List<RolePermissionRef> rolePermissionRefs = new ArrayList<>();

        if (!CollectionUtils.isEmpty(roleIds)) {
            QueryWrapperBuilder builder = QueryWrapper.newBuilder();
            builder.in("role_id", roleIds);
            rolePermissionRefs = rolePermissionRefDAO.selectByCondition(builder.build());
        }
        return rolePermissionRefs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RolePermissionRef> findByPermissionIds(List<String> permissionIds) {
        List<RolePermissionRef> rolePermissionRefs = new ArrayList<>();

        if (CollectionUtils.isEmpty(permissionIds)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.in("permission_id", permissionIds);
            rolePermissionRefs = rolePermissionRefDAO.selectByCondition(builder.build());
        }

        return rolePermissionRefs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleId(String roleId) {
        if (!StringUtils.isEmpty(roleId)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.eq("role_id", roleId);
            rolePermissionRefDAO.deleteByCondition(builder.build());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<RolePermissionRef> rolePermissionRefs) {
        if (!CollectionUtils.isEmpty(rolePermissionRefs)) {
            rolePermissionRefDAO.batchInsert(rolePermissionRefs);
        }
    }
}
