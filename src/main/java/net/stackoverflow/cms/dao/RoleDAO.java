package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (Role)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-08-15 11:12:20
 */
@Mapper
public interface RoleDAO {

    int countByCondition(QueryWrapper wrapper);

    Role select(String id);

    List<Role> selectByCondition(QueryWrapper wrapper);

    int insert(Role role);

    int batchInsert(List<Role> roles);

    int delete(String id);

    int batchDelete(List<String> ids);

    int deleteByCondition(QueryWrapper wrapper);

    int update(Role role);

    int batchUpdate(List<Role> roles);

    int updateByCondition(QueryWrapper wrapper);
}