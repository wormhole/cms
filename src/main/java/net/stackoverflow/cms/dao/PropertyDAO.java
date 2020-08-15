package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.Property;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (Property)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-08-15 11:16:04
 */
@Mapper
public interface PropertyDAO {

    int countByCondition(QueryWrapper wrapper);

    Property select(String id);

    List<Property> selectByCondition(QueryWrapper wrapper);

    int insert(Property property);

    int batchInsert(List<Property> propertys);

    int delete(String id);

    int batchDelete(List<String> ids);

    int deleteByCondition(QueryWrapper wrapper);

    int update(Property property);

    int batchUpdate(List<Property> propertys);

    int updateByCondition(QueryWrapper wrapper);
}