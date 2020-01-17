package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.Token;

import java.util.List;
import java.util.Map;

public interface TokenService {

    List<Token> findByPage(Page page);

    List<Token> findByCondition(Map<String, Object> condition);

    List<Token> findAll();

    Token findById(String id);

    List<Token> findByIds(List<String> ids);

    void save(Token token);

    void batchSave(List<Token> tokens);

    void delete(String id);

    void batchDelete(List<String> ids);

    void update(Token token);

    void batchUpdate(List<Token> tokens);
}
