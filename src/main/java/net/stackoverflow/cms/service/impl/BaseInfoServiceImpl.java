package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.constant.BaseInfoConst;
import net.stackoverflow.cms.model.dto.BaseInfoDTO;
import net.stackoverflow.cms.model.dto.PropertyDTO;
import net.stackoverflow.cms.model.entity.Upload;
import net.stackoverflow.cms.service.BaseInfoService;
import net.stackoverflow.cms.service.PropertyService;
import net.stackoverflow.cms.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基础信息服务类接口
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class BaseInfoServiceImpl implements BaseInfoService {

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private UploadService uploadService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseInfoDTO queryBaseInfo() {
        List<PropertyDTO> dtos = propertyService.findByKeys(Arrays.asList("title", "head", "copyright"));
        BaseInfoDTO baseInfo = new BaseInfoDTO();
        for (PropertyDTO dto : dtos) {
            if ("head".equals(dto.getKey())) {
                baseInfo.setHead(dto.getValue());
            } else if ("title".equals(dto.getKey())) {
                baseInfo.setTitle(dto.getValue());
            } else if ("copyright".equals(dto.getKey())) {
                baseInfo.setCopyright(dto.getValue());
            }
        }
        return baseInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBaseInfo(BaseInfoDTO dto) {
        List<PropertyDTO> dtos = new ArrayList<>();
        dtos.add(new PropertyDTO("head", dto.getHead()));
        dtos.add(new PropertyDTO("title", dto.getTitle()));
        dtos.add(new PropertyDTO("copyright", dto.getCopyright()));
        propertyService.batchUpdateByKey(dtos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHead(String userId, MultipartFile file) throws IOException {
        Upload upload = uploadService.saveFile(file, userId);
        propertyService.updateByKey("head", upload.getPath());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseInfoDTO restore() {
        List<PropertyDTO> dtos = new ArrayList<>();
        dtos.add(new PropertyDTO("title", BaseInfoConst.TITLE));
        dtos.add(new PropertyDTO("copyright", BaseInfoConst.COPYRIGHT));
        dtos.add(new PropertyDTO("head", BaseInfoConst.HEAD));
        propertyService.batchUpdateByKey(dtos);

        BaseInfoDTO baseInfo = new BaseInfoDTO();
        baseInfo.setHead(BaseInfoConst.HEAD);
        baseInfo.setCopyright(BaseInfoConst.COPYRIGHT);
        baseInfo.setTitle(BaseInfoConst.TITLE);
        return baseInfo;
    }
}
