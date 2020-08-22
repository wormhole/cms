package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.Upload;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Upload)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-08-22 16:49:33
 */
@Repository
public interface UploadDAO {

    /**
     * 根据条件统计
     *
     * @param wrapper
     * @return
     */
    int countByCondition(QueryWrapper wrapper);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    Upload select(String id);

    /**
     * 条件查询
     *
     * @param wrapper
     * @return
     */
    List<Upload> selectByCondition(QueryWrapper wrapper);

    /**
     * 新增
     *
     * @param upload
     * @return
     */
    int insert(Upload upload);

    /**
     * 批量新增
     *
     * @param uploads
     * @return
     */
    int batchInsert(List<Upload> uploads);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    int batchDelete(List<String> ids);

    /**
     * 根据条件删除
     *
     * @param wrapper
     * @return
     */
    int deleteByCondition(QueryWrapper wrapper);

    /**
     * 更新
     *
     * @param upload
     * @return
     */
    int update(Upload upload);

    /**
     * 批量更新
     *
     * @param uploads
     * @return
     */
    int batchUpdate(List<Upload> uploads);

    /**
     * 根据条件更新
     *
     * @param wrapper
     * @return
     */
    int updateByCondition(QueryWrapper wrapper);
}