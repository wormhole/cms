package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.Config;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ConfigDAO {

    List<Config> selectByPage(Page page);

    List<Config> selectByCondition(Map<String, Object> condition);

    Config select(String id);

    int insert(Config config);

    int batchInsert(List<Config> configs);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(Config config);

    int batchUpdate(List<Config> configs);

    int updateByKey(Config config);

    int batchUpdateByKey(List<Config> configs);

}
