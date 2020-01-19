package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.File;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FileDAO {

    List<File> selectByPage(Page page);

    List<File> selectByCondition(Map<String, Object> condition);

    File select(String id);

    int insert(File file);

    int batchInsert(List<File> files);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(File file);

    int batchUpdate(List<File> files);

}
