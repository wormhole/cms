package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.pojo.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PermissionDAO {

    List<Permission> selectByPage(Page page);

    List<Permission> selectByCondition(Map<String, Object> searchMap);

    Permission select(String id);

    int insert(Permission permission);

    int batchInsert(List<Permission> permissions);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(Permission permission);

    int batchUpdate(List<Permission> permissions);

}
