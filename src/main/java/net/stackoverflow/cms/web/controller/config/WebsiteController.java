package net.stackoverflow.cms.web.controller.config;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.constant.UploadPathConst;
import net.stackoverflow.cms.model.dto.PropertyDTO;
import net.stackoverflow.cms.service.PropertyService;
import net.stackoverflow.cms.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.*;

/**
 * 网站配置接口
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/config/website")
@Slf4j
@Validated
public class WebsiteController extends BaseController {

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private UploadService uploadService;

    private final String TITLE = "内容管理系统";
    private final String COPYRIGHT = "copyright © 2020 by 凉衫薄";
    private final String HEAD = "default";
    private final String REMEMBER_ME = "false";

    /**
     * 获取配置信息
     *
     * @return
     */
    @GetMapping(value = "/properties")
    public ResponseEntity<Result<Map<String, String>>> list() {
        List<PropertyDTO> propertyDTOS = propertyService.findByKeys(Arrays.asList("title", "copyright", "head", "rememberMe"));
        Map<String, String> map = new HashMap<>(16);
        for (PropertyDTO propertyDTO : propertyDTOS) {
            if ("head".equals(propertyDTO.getKey())) {
                if ("default".equals(propertyDTO.getValue())) {
                    map.put(propertyDTO.getKey(), "/head.jpg");
                } else {
                    map.put(propertyDTO.getKey(), UploadPathConst.PREFIX + propertyDTO.getValue());
                }
            } else {
                map.put(propertyDTO.getKey(), propertyDTO.getValue());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", map));
    }

    /**
     * 更新配置
     *
     * @param propertyDTOS
     * @return
     */
    @PutMapping(value = "/properties")
    public ResponseEntity<Result<Object>> update(@RequestBody @NotEmpty(message = "参数不能为空") List<PropertyDTO> propertyDTOS) {
        propertyService.batchUpdateByKey(propertyDTOS);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));

    }

    /**
     * 更新头像
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/head")
    public ResponseEntity<Result<Object>> head(@RequestParam("file") MultipartFile file) throws IOException {
        uploadService.updateHead(file, super.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));
    }

    /**
     * 还原默认配置
     *
     * @return
     */
    @PutMapping(value = "/restore")
    public ResponseEntity<Result<Map<String, String>>> restore() {
        List<PropertyDTO> propertyDTOS = new ArrayList<>();

        PropertyDTO title = new PropertyDTO();
        title.setKey("title");
        title.setValue(TITLE);

        PropertyDTO copyright = new PropertyDTO();
        copyright.setKey("copyright");
        copyright.setValue(COPYRIGHT);

        PropertyDTO head = new PropertyDTO();
        head.setKey("head");
        head.setValue(HEAD);

        PropertyDTO rememberMe = new PropertyDTO();
        rememberMe.setKey("rememberMe");
        rememberMe.setValue(REMEMBER_ME);

        propertyDTOS.add(title);
        propertyDTOS.add(copyright);
        propertyDTOS.add(head);
        propertyDTOS.add(rememberMe);

        Map<String, String> map = new HashMap<>(16);
        for (PropertyDTO propertyDTO : propertyDTOS) {
            if ("head".equals(propertyDTO.getKey())) {
                map.put(propertyDTO.getKey(), "/head.jpg");
            } else {
                map.put(propertyDTO.getKey(), propertyDTO.getValue());
            }
        }

        propertyService.batchUpdateByKey(propertyDTOS);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", map));
    }
}
