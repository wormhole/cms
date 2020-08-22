package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.UserRoleRef;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (UserRoleRef)表数据库访问层
 *
 * @author minsheng.cai
 * @since 2020-08-22 13:34:45
 */
@Repository
public interface UserRoleRefDAO {

    int countByCondition(QueryWrapper wrapper);

    UserRoleRef select(String id);

    List<UserRoleRef> selectByCondition(QueryWrapper wrapper);

    int insert(UserRoleRef userRoleRef);

    int batchInsert(List<UserRoleRef> userRoleRefs);

    int delete(String id);

    int batchDelete(List<String> ids);

    int deleteByCondition(QueryWrapper wrapper);

    int update(UserRoleRef userRoleRef);

    int batchUpdate(List<UserRoleRef> userRoleRefs);

    int updateByCondition(QueryWrapper wrapper);
}