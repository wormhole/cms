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

    /**
     * 查询用户文件数量
     *
     * @param userId
     * @return
     */
    Integer count();

    /**
     * 保存文件
     *
     * @param file
     * @param userId
     * @return
     * @throws IOException
     */
    Upload saveFile(MultipartFile file, String userId) throws IOException;

    /**
     * 更新头像
     *
     * @param file
     * @param userId
     * @throws IOException
     */
    void updateHead(MultipartFile file, String userId) throws IOException;

    /**
     * 分页查询上传图片
     *
     * @param page   当前页
     * @param limit  每页大小
     * @param sort   排序字段
     * @param order  排序方式
     * @param key    关键字
     * @param userId 用户主键过滤
     * @return
     */
    PageResponse<UploadDTO> findImageByPage(Integer page, Integer limit, String sort, String order, String key, String userId);

    /**
     * 根据主键列表删除
     *
     * @param ids
     */
    void deleteByIds(List<String> ids);
}
