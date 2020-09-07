package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.dto.BaseInfoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 基础性信息服务类解耦
 *
 * @author 凉衫薄
 */
public interface BaseInfoService {

    /**
     * 查询系统基础信息
     *
     * @return
     */
    BaseInfoDTO queryBaseInfo();

    /**
     * 更新系统基础信息
     *
     * @param dto 系统基础信息dto对象
     */
    void updateBaseInfo(BaseInfoDTO dto);

    /**
     * 更新头像
     *
     * @param userId 用户主键
     * @param file   文件对象
     */
    void updateHead(String userId, MultipartFile file) throws IOException;

    /**
     * 还原默认配置
     *
     * @return
     */
    BaseInfoDTO restore();
}
