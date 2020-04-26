package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.constant.UploadConst;
import net.stackoverflow.cms.dao.FileDAO;
import net.stackoverflow.cms.model.entity.File;
import net.stackoverflow.cms.service.FileService;
import net.stackoverflow.cms.util.SysUtils;
import net.stackoverflow.cms.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public File saveFile(MultipartFile file, String userId) throws IOException {
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.lastIndexOf("."));
        String path = TimeUtils.pathWithDate() + UUID.randomUUID().toString() + ext;
        String uploadPath = SysUtils.isWin() ? UploadConst.UPLOAD_PATH_WINDOWS : UploadConst.UPLOAD_PATH_LINUX;
        String absolutePath = uploadPath + path;
        java.io.File uploadFile = new java.io.File(absolutePath);
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.mkdirs();
        }
        file.transferTo(uploadFile);
        File filePO = new File(UUID.randomUUID().toString(), filename, path, new Date(), userId);
        fileDAO.insert(filePO);
        return filePO;
    }

    @Override
    public String getFileUrl(String fileId) {
        File file = fileDAO.select(fileId);
        return UploadConst.PREFIX + file.getPath();
    }

}
