package net.stackoverflow.cms.service;

import net.stackoverflow.cms.dao.ConfigDAO;
import net.stackoverflow.cms.model.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigDAO configDAO;

    @Override
    public List<Config> findByCondition(Map<String, Object> condition) {
        return configDAO.selectByCondition(condition);
    }

    @Override
    public Config findByKey(String key) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("key", key);
        List<Config> configs = configDAO.selectByCondition(condition);
        if (configs != null && configs.size() > 0) {
            return configs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Config> findAll() {
        return configDAO.selectByCondition(new HashMap<>());
    }

    @Override
    public Config findById(String id) {
        return configDAO.select(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Config config) {
        configDAO.update(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdate(List<Config> configs) {
        configDAO.batchUpdate(configs);
    }

}
