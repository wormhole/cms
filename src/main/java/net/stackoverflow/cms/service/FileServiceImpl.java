package net.stackoverflow.cms.service;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.dao.FileDAO;
import net.stackoverflow.cms.model.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private FileDAO fileDAO;

    @Override
    public List<File> findByCondition(Map<String, Object> condition) {
        return fileDAO.selectByCondition(condition);
    }

    @Override
    public File findById(String id) {
        return fileDAO.select(id);
    }

    @Override
    public List<File> findByIds(List<String> ids) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("ids", ids);
        return fileDAO.selectByCondition(condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(File file) {
        fileDAO.insert(file);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        fileDAO.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(File file) {
        fileDAO.update(file);
    }

}
