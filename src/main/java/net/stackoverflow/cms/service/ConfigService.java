package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.entity.Config;

import java.util.List;
import java.util.Map;

public interface ConfigService {

    List<Config> findByCondition(Map<String, Object> condition);

    Config findByKey(String key);

    List<Config> findAll();

    void update(Config config);

    void batchUpdate(List<Config> configs);

    void updateByKey(Config config);

    void batchUpdateByKey(List<Config> configs);
}
