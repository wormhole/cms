package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.pojo.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService {

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
