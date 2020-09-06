package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.PropertyDTO;
import net.stackoverflow.cms.model.dto.SysDTO;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基础信息接口
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/property")
@Slf4j
@Validated
public class PropertyController extends BaseController {

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private UploadService uploadService;

    private final String TITLE = "内容管理系统";
    private final String COPYRIGHT = "copyright © 2020 by 凉衫薄";
    private final String HEAD = "/head.jpg";

    /**
     * 获取配置信息
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<Result<SysDTO>> queryConfiguration() {
        List<PropertyDTO> dtos = propertyService.findByKeys(Arrays.asList("title", "head", "copyright"));
        SysDTO sys = new SysDTO();
        for (PropertyDTO dto : dtos) {
            if ("head".equals(dto.getKey())) {
                sys.setHead(dto.getValue());
            } else if ("title".equals(dto.getKey())) {
                sys.setTitle(dto.getValue());
            } else if ("copyright".equals(dto.getKey())) {
                sys.setCopyright(dto.getValue());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(Result.success(sys));
    }

    /**
     * 更新配置
     *
     * @param dtos
     * @return
     */
    @PutMapping
    public ResponseEntity<Result<Object>> updateConfiguration(@RequestBody @NotEmpty(message = "参数不能为空") List<PropertyDTO> dtos) {
        propertyService.batchUpdateByKey(dtos);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());

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
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 还原默认配置
     *
     * @return
     */
    @PutMapping(value = "/restore")
    public ResponseEntity<Result<SysDTO>> restore() {
        List<PropertyDTO> dtos = new ArrayList<>();

        PropertyDTO title = new PropertyDTO();
        title.setKey("title");
        title.setValue(TITLE);

        PropertyDTO copyright = new PropertyDTO();
        copyright.setKey("copyright");
        copyright.setValue(COPYRIGHT);

        PropertyDTO head = new PropertyDTO();
        head.setKey("head");
        head.setValue(HEAD);

        dtos.add(title);
        dtos.add(copyright);
        dtos.add(head);

        SysDTO sys = new SysDTO();
        for (PropertyDTO dto : dtos) {
            if ("head".equals(dto.getKey())) {
                sys.setHead(dto.getValue());
            } else if ("title".equals(dto.getKey())) {
                sys.setTitle(dto.getValue());
            } else if ("copyright".equals(dto.getKey())) {
                sys.setCopyright(dto.getValue());
            }
        }

        propertyService.batchUpdateByKey(dtos);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(sys));
    }
}
