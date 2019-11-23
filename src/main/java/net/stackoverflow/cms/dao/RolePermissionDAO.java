package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.pojo.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RolePermissionDAO {

    List<RolePermission> selectByPage(Page page);

    List<RolePermission> selectByCondition(Map<String, Object> searchMap);

    RolePermission select(String id);

    int insert(RolePermission rolePermission);

    int batchInsert(List<RolePermission> rolePermissions);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(String id);

    int batchUpdate(List<RolePermission> rolePermissions);
}
