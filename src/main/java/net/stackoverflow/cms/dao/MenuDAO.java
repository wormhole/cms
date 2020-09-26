package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Menu)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-09-26 20:22:30
 */
@Repository
public interface MenuDAO {

    /**
     * 根据条件统计
     *
     * @param wrapper
     * @return
     */
    int countWithQuery(QueryWrapper wrapper);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    Menu select(String id);

    /**
     * 查询所有
     *
     * @return
     */
    List<Menu> selectAll();

    /**
     * 条件查询
     *
     * @param wrapper
     * @return
     */
    List<Menu> selectWithQuery(QueryWrapper wrapper);

    /**
     * 新增
     *
     * @param menu
     * @return
     */
    int insert(Menu menu);

    /**
     * 批量新增
     *
     * @param menus
     * @return
     */
    int batchInsert(List<Menu> menus);

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
    int deleteWithQuery(QueryWrapper wrapper);

    /**
     * 更新
     *
     * @param menu
     * @return
     */
    int update(Menu menu);

    /**
     * 批量更新
     *
     * @param menus
     * @return
     */
    int batchUpdate(List<Menu> menus);

    /**
     * 根据条件更新
     *
     * @param wrapper
     * @return
     */
    int updateWithQuery(QueryWrapper wrapper);
}