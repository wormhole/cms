package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.RoleMenuRef;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (RoleMenuRef)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-09-04 23:34:30
 */
@Repository
public interface RoleMenuRefDAO {

    int countByCondition(QueryWrapper wrapper);

    RoleMenuRef select(String id);

    List<RoleMenuRef> selectByCondition(QueryWrapper wrapper);

    int insert(RoleMenuRef roleMenuRef);

    int batchInsert(List<RoleMenuRef> roleMenuRefs);

    int delete(String id);

    int batchDelete(List<String> ids);

    int deleteByCondition(QueryWrapper wrapper);

    int update(RoleMenuRef roleMenuRef);

    int batchUpdate(List<RoleMenuRef> roleMenuRefs);

    int updateByCondition(QueryWrapper wrapper);
}