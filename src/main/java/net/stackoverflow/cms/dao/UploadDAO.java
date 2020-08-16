package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.Upload;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Upload)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-08-15 23:24:40
 */
@Repository
public interface UploadDAO {

    int countByCondition(QueryWrapper wrapper);

    Upload select(String id);

    List<Upload> selectByCondition(QueryWrapper wrapper);

    int insert(Upload upload);

    int batchInsert(List<Upload> uploads);

    int delete(String id);

    int batchDelete(List<String> ids);

    int deleteByCondition(QueryWrapper wrapper);

    int update(Upload upload);

    int batchUpdate(List<Upload> uploads);

    int updateByCondition(QueryWrapper wrapper);
}