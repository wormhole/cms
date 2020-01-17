package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.entity.Token;

import java.util.List;
import java.util.Map;

public interface TokenService {

    List<Token> findByCondition(Map<String, Object> condition);

    List<Token> findAll();

    Token findById(String id);

    void save(Token token);

    void delete(String id);

    void update(Token token);
}
