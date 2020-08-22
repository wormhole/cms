package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Permission)表数据库访问层
 *
 * @author minsheng.cai
 * @since 2020-08-22 11:41:59
 */
@Repository
public interface PermissionDAO {

    int countByCondition(QueryWrapper wrapper);

    Permission select(String id);

    List<Permission> selectByCondition(QueryWrapper wrapper);

    int insert(Permission permission);

    int batchInsert(List<Permission> permissions);

    int delete(String id);

    int batchDelete(List<String> ids);

    int deleteByCondition(QueryWrapper wrapper);

    int update(Permission permission);

    int batchUpdate(List<Permission> permissions);

    int updateByCondition(QueryWrapper wrapper);
}