package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.common.QueryWrapper.QueryWrapperBuilder;
import net.stackoverflow.cms.dao.RoleMenuRefDAO;
import net.stackoverflow.cms.model.entity.RoleMenuRef;
import net.stackoverflow.cms.service.RoleMenuRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 角色菜单关联服务实现类
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class RoleMenuRefServiceImpl implements RoleMenuRefService {

    @Autowired
    private RoleMenuRefDAO roleMenuRefDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIds(List<String> roleIds) {
        if (!CollectionUtils.isEmpty(roleIds)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.in("role_id", roleIds);
            roleMenuRefDAO.queryDelete(builder.build());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleId(String roleId) {
        roleMenuRefDAO.queryDelete(QueryWrapper.newBuilder().eq("role_id", roleId).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<RoleMenuRef> refs) {
        if (!CollectionUtils.isEmpty(refs)) {
            roleMenuRefDAO.batchInsert(refs);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RoleMenuRef> findByRoleId(String id) {
        List<RoleMenuRef> refs = roleMenuRefDAO.querySelect(QueryWrapper.newBuilder().eq("role_id", id).build());
        return refs;
    }
}
