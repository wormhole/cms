package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.Setting;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Setting)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-09-26 19:03:51
 */
@Repository
public interface SettingDAO {

    /**
     * 根据条件统计
     *
     * @param wrapper
     * @return
     */
    int queryCount(QueryWrapper wrapper);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    Setting select(String id);

    /**
     * 条件查询
     *
     * @param wrapper
     * @return
     */
    List<Setting> querySelect(QueryWrapper wrapper);

    /**
     * 新增
     *
     * @param setting
     * @return
     */
    int insert(Setting setting);

    /**
     * 批量新增
     *
     * @param settings
     * @return
     */
    int batchInsert(List<Setting> settings);

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
    int queryDelete(QueryWrapper wrapper);

    /**
     * 更新
     *
     * @param setting
     * @return
     */
    int update(Setting setting);

    /**
     * 批量更新
     *
     * @param settings
     * @return
     */
    int batchUpdate(List<Setting> settings);

    /**
     * 根据条件更新
     *
     * @param wrapper
     * @return
     */
    int queryUpdate(QueryWrapper wrapper);
}