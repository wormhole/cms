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
    public SettingDTO querySetting() {
        Setting setting = settingDAO.selectAll().get(0);
        SettingDTO dto = new SettingDTO();
        BeanUtils.copyProperties(setting, dto);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SettingDTO dto) {
        QueryWrapper.QueryWrapperBuilder builder = new QueryWrapper.QueryWrapperBuilder();
        builder.set("title", dto.getTitle());
        builder.set("copyright", dto.getCopyright());
        builder.set("ts", new Date());
        settingDAO.updateWithQuery(builder.build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHead(String userId, MultipartFile file) throws IOException {
        Upload upload = uploadService.saveFile(file, userId);
        QueryWrapper.QueryWrapperBuilder builder = new QueryWrapper.QueryWrapperBuilder();
        builder.set("head", upload.getPath());
        builder.set("ts", new Date());
        settingDAO.updateWithQuery(builder.build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SettingDTO restore() {
        QueryWrapper.QueryWrapperBuilder builder = new QueryWrapper.QueryWrapperBuilder();
        builder.set("title", SettingConst.TITLE);
        builder.set("head", SettingConst.HEAD);
        builder.set("copyright", SettingConst.COPYRIGHT);
        builder.set("ts", new Date());
        settingDAO.updateWithQuery(builder.build());

        SettingDTO dto = new SettingDTO();
        dto.setHead(SettingConst.HEAD);
        dto.setCopyright(SettingConst.COPYRIGHT);
        dto.setTitle(SettingConst.TITLE);
        return dto;
    }
}
