package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.QueryWrapper.QueryWrapperBuilder;
import net.stackoverflow.cms.dao.UserRoleRefDAO;
import net.stackoverflow.cms.model.entity.UserRoleRef;
import net.stackoverflow.cms.service.UserRoleRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户角色关联服务实现类
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class UserRoleRefServiceImpl implements UserRoleRefService {

    @Autowired
    private UserRoleRefDAO userRoleRefDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserRoleRef> findByUserId(String userId) {
        List<UserRoleRef> userRoleRefs = new ArrayList<>();
        if (!StringUtils.isEmpty(userId)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.eq("user_id", userId);
            userRoleRefs = userRoleRefDAO.querySelect(builder.build());
        }
        return userRoleRefs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserRoleRef> findByRoleIds(List<String> roleIds) {
        List<UserRoleRef> userRoleRefs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleIds)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.in("role_id", roleIds);
            userRoleRefs = userRoleRefDAO.querySelect(builder.build());
        }
        return userRoleRefs;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserIds(List<String> userIds) {
        if (!CollectionUtils.isEmpty(userIds)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.in("user_id", userIds);
            userRoleRefDAO.queryDelete(builder.build());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserId(String userId) {
        if (!StringUtils.isEmpty(userId)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.eq("user_id", userId);
            userRoleRefDAO.queryDelete(builder.build());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIds(List<String> roleIds) {
        if (!CollectionUtils.isEmpty(roleIds)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.in("role_id", roleIds);
            userRoleRefDAO.queryDelete(builder.build());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<UserRoleRef> userRoleRefs) {
        if (!CollectionUtils.isEmpty(userRoleRefs)) {
            userRoleRefDAO.batchInsert(userRoleRefs);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserRoleRef ref) {
        userRoleRefDAO.insert(ref);
    }
}
