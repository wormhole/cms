package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.dao.TokenDAO;
import net.stackoverflow.cms.model.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenDAO tokenDAO;

    @Override
    public List<Token> findByPage(Page page) {
        return tokenDAO.selectByPage(page);
    }

    @Override
    public List<Token> findByCondition(Map<String, Object> condition) {
        return tokenDAO.selectByCondition(condition);
    }

    @Override
    public List<Token> findAll() {
        return tokenDAO.selectByCondition(new HashMap<>());
    }

    @Override
    public Token findById(String id) {
        return tokenDAO.select(id);
    }

    @Override
    public List<Token> findByIds(List<String> ids) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("ids", ids);
        return tokenDAO.selectByCondition(condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Token token) {
        tokenDAO.insert(token);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(List<Token> tokens) {
        tokenDAO.batchInsert(tokens);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        tokenDAO.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        tokenDAO.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Token token) {
        tokenDAO.update(token);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<Token> tokens) {
        tokenDAO.batchUpdate(tokens);
    }

}
