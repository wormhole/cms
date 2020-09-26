package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.constant.SettingConst;
import net.stackoverflow.cms.dao.SettingDAO;
import net.stackoverflow.cms.model.dto.SettingDTO;
import net.stackoverflow.cms.model.entity.Setting;
import net.stackoverflow.cms.model.entity.Upload;
import net.stackoverflow.cms.service.SettingService;
import net.stackoverflow.cms.service.UploadService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * 系统设置服务实现类
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingDAO settingDAO;
    @Autowired
    private UploadService uploadService;

    @Override
    public SettingDTO getSetting() {
        Setting setting = settingDAO.selectWithQuery(QueryWrapper.newBuilder().build()).get(0);
        SettingDTO dto = new SettingDTO();
        BeanUtils.copyProperties(setting, dto);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SettingDTO dto) {
        Setting setting = settingDAO.selectWithQuery(QueryWrapper.newBuilder().build()).get(0);
        setting.setTitle(dto.getTitle());
        setting.setCopyright(dto.getCopyright());
        setting.setTs(new Date());
        settingDAO.update(setting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHead(String userId, MultipartFile file) throws IOException {
        Upload upload = uploadService.saveFile(file, userId);
        Setting setting = settingDAO.selectWithQuery(QueryWrapper.newBuilder().build()).get(0);
        setting.setHead(upload.getPath());
        setting.setTs(new Date());
        settingDAO.update(setting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettingDTO restore() {
        Setting setting = settingDAO.selectWithQuery(QueryWrapper.newBuilder().build()).get(0);
        setting.setTitle(SettingConst.TITLE);
        setting.setCopyright(SettingConst.COPYRIGHT);
        setting.setHead(SettingConst.HEAD);
        setting.setTs(new Date());
        settingDAO.update(setting);

        SettingDTO dto = new SettingDTO();
        BeanUtils.copyProperties(setting, dto);
        return dto;
    }
}
