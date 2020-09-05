package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.model.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Menu)表数据库访问层
 *
 * @author 凉衫薄
 * @since 2020-09-04 23:25:06
 */
@Repository
public interface MenuDAO {

    int countByCondition(QueryWrapper wrapper);

    Menu select(String id);

    List<Menu> selectByCondition(QueryWrapper wrapper);

    int insert(Menu menu);

    int batchInsert(List<Menu> menus);

    int delete(String id);

    int batchDelete(List<String> ids);

    int deleteByCondition(QueryWrapper wrapper);

    int update(Menu menu);

    int batchUpdate(List<Menu> menus);

    int updateByCondition(QueryWrapper wrapper);
}