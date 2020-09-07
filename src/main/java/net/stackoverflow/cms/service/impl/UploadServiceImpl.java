package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.common.QueryWrapper.QueryWrapperBuilder;
import net.stackoverflow.cms.constant.FileTypeConst;
import net.stackoverflow.cms.constant.UploadPathConst;
import net.stackoverflow.cms.dao.UploadDAO;
import net.stackoverflow.cms.model.dto.UploadDTO;
import net.stackoverflow.cms.model.entity.Upload;
import net.stackoverflow.cms.service.UploadService;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.FileUtils;
import net.stackoverflow.cms.util.SysUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 文件上传服务实现类
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Autowired
    private UploadDAO uploadDAO;
    @Autowired
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer count() {
        return uploadDAO.queryCount(QueryWrapper.newBuilder().build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Upload saveFile(MultipartFile file, String userId) throws IOException {
        String filename = file.getOriginalFilename();
        String ext = filename.substring(filename.lastIndexOf("."));
        String filePath = FileUtils.pathWithDate() + UUID.randomUUID().toString() + ext;
        String uploadPath = SysUtils.pwd() + UploadPathConst.UPLOAD_PATH;
        String absolutePath = uploadPath + filePath;
        java.io.File uploadFile = new java.io.File(absolutePath);
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.mkdirs();
        }
        file.transferTo(uploadFile);
        Upload upload = new Upload(UUID.randomUUID().toString(), filename, UploadPathConst.UPLOAD_PATH + filePath, new Date(), userId, FileUtils.getType(ext.substring(1)));
        uploadDAO.insert(upload);
        return upload;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResponse<UploadDTO> findImageByPage(Integer page, Integer limit, String sort, String order, String key, String userId) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        if (StringUtils.isEmpty(sort) || StringUtils.isEmpty(order)) {
            builder.sort("ts", "desc");
        } else {
            builder.sort(sort, order);
        }
        builder.eq("type", FileTypeConst.T_IMAGE);
        builder.eq("user_id", userId);
        builder.like(!StringUtils.isEmpty(key), key, Arrays.asList("name", "path"));
        builder.page((page - 1) * limit, limit);
        QueryWrapper wrapper = builder.build();

        List<Upload> uploads = uploadDAO.querySelect(wrapper);
        Integer total = uploadDAO.queryCount(wrapper);

        List<UploadDTO> dtos = new ArrayList<>();
        for (Upload upload : uploads) {
            UploadDTO dto = new UploadDTO();
            BeanUtils.copyProperties(upload, dto);
            dto.setUsername(userService.findById(dto.getUserId()).getUsername());
            String path = SysUtils.pwd() + dto.getPath();
            if (SysUtils.isWin()) {
                path = path.replace("/", "\\");
            } else {
                path = path.replace("\\", "/");
            }
            dto.setAbsolutePath(path);
            dtos.add(dto);
        }

        return new PageResponse<>(total, dtos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.in("id", ids);
            List<Upload> uploads = uploadDAO.querySelect(builder.build());
            for (Upload upload : uploads) {
                String path = SysUtils.pwd() + upload.getPath();
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
            uploadDAO.batchDelete(ids);
        }
    }

}
