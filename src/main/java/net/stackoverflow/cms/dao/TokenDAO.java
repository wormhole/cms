package net.stackoverflow.cms.dao;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.Token;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TokenDAO {

    List<Token> selectByPage(Page page);

    List<Token> selectByCondition(Map<String, Object> condition);

    Token select(String id);

    int insert(Token token);

    int batchInsert(List<Token> tokens);

    int delete(String id);

    int batchDelete(List<String> ids);

    int update(Token token);

    int batchUpdate(List<Token> tokens);

}
