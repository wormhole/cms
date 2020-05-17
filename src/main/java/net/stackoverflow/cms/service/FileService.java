package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.model.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FileService {

    List<File> findByPage(Page page);

    List<File> findByCondition(Map<String, Object> condition);

    File findById(String id);

    List<File> findByIds(List<String> ids);

    void save(File file);

    void batchDelete(List<String> ids);

    void update(File file);

    File saveFile(MultipartFile file, String userId) throws IOException;

    String getFileUrl(String fileId);
}
