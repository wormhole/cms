package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.RolePermissionRef;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (RolePermissionRef)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-08-15 11:12:35
 */
@Mapper
public interface RolePermissionRefDAO {

    int countByCondition(QueryWrapper wrapper);

    RolePermissionRef select(String id);

    List<RolePermissionRef> selectByCondition(QueryWrapper wrapper);

    int insert(RolePermissionRef rolePermissionRef);

    int batchInsert(List<RolePermissionRef> rolePermissionRefs);

    int delete(String id);

    int batchDelete(List<String> ids);

    int deleteByCondition(QueryWrapper wrapper);

    int update(RolePermissionRef rolePermissionRef);

    int batchUpdate(List<RolePermissionRef> rolePermissionRefs);

    int updateByCondition(QueryWrapper wrapper);
}