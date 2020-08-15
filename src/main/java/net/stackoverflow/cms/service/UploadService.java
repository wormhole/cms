package net.stackoverflow.cms.service;

import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.model.dto.UploadDTO;
import net.stackoverflow.cms.model.entity.Upload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 文件上传服务接口
 *
 * @author 凉衫薄
 */
public interface UploadService {

    Upload saveFile(MultipartFile file, String userId) throws IOException;

    void updateHead(MultipartFile file, String userId) throws IOException;

    PageResponse<UploadDTO> findImageByPage(Integer page, Integer limit, String sort, String order, String key, String userId);

    void deleteByIds(List<String> ids);
}
