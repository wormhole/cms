package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.Config;

import java.util.List;
import java.util.Map;

public interface ConfigService {

    List<Config> findByPage(Page page);

    List<Config> findByCondition(Map<String, Object> condition);

    List<Config> findAll();

    Config findById(String id);

    List<Config> findByIds(List<String> ids);

    void save(Config config);

    void batchSave(List<Config> configs);

    void delete(String id);

    void batchDelete(List<String> ids);

    void update(Config config);

    void batchUpdate(List<Config> configs);
}
