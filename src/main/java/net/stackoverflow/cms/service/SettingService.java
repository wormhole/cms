package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.dto.SettingDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 系统设置服务接口
 *
 * @author 凉衫薄
 */
public interface SettingService {

    /**
     * 获取系统配置信息
     *
     * @return
     */
    SettingDTO querySetting();

    /**
     * 更新系统设置
     *
     * @param dto 系统设置dto类
     */
    void update(SettingDTO dto);

    /**
     * 更新头像
     *
     * @param userId 用户主键
     * @param file   头像文件
     * @throws IOException io异常
     */
    void updateHead(String userId, MultipartFile file) throws IOException;

    /**
     * 还原默认配置
     *
     * @return
     */
    SettingDTO restore();
}
