package net.stackoverflow.cms.web.controller.config;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Config;
import net.stackoverflow.cms.model.entity.File;
import net.stackoverflow.cms.model.vo.ConfigVO;
import net.stackoverflow.cms.service.ConfigService;
import net.stackoverflow.cms.service.FileService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统设置接口
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/config")
@Slf4j
public class ConfigController extends BaseController {

    @Autowired
    private ConfigService configService;
    @Value("${application.upload-path}")
    private String uploadPath;
    @Value("${application.static-prefix}")
    private String prefix;
    @Autowired
    private FileService fileService;

    private final String TITLE = "内容管理系统";
    private final String COPYRIGHT = "copyright © 2020 by 凉衫薄";
    private final String HEAD = "default";

    /**
     * 获取配置信息
     *
     * @return
     */
    @GetMapping(value = "/info")
    public ResponseEntity config() {
        Result result = new Result();

        List<Config> configs = configService.findAll();
        List<ConfigVO> configVOs = new ArrayList<>();
        for (Config config : configs) {
            ConfigVO configVO = new ConfigVO();
            BeanUtils.copyProperties(config, configVO);
            if (configVO.getKey().equals("head") && !configVO.getValue().equals("default")) {
                configVO.setValue(prefix + configVO.getValue());
            }
            configVOs.add(configVO);
        }
        result.setMessage("success");
        result.setStatus(Result.Status.SUCCESS);
        result.setData(configVOs);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 更新配置
     *
     * @param configVOs
     * @return
     */
    @PutMapping(value = "/update")
    public ResponseEntity update(@RequestBody List<ConfigVO> configVOs) {
        Result result = new Result();

        if (configVOs == null || configVOs.size() == 0) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("参数不能为空");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        List<Config> configs = new ArrayList<>();
        for (ConfigVO configVO : configVOs) {
            Config config = new Config();
            BeanUtils.copyProperties(configVO, config);
            configs.add(config);
        }
        configService.batchUpdate(configs);
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 更新头像
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/head")
    public ResponseEntity head(@RequestParam("file") MultipartFile file) throws IOException {
        Result result = new Result();

        File filePO = saveFile(file, uploadPath, fileService);
        Config config = configService.findByKey("head");
        config.setValue(filePO.getPath());
        configService.update(config);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 还原默认配置
     *
     * @return
     */
    @PutMapping(value = "/restore")
    public ResponseEntity restore() {
        Result result = new Result();

        List<Config> configs = configService.findAll();
        for (Config config : configs) {
            switch (config.getKey()) {
                case "title":
                    config.setValue(TITLE);
                    break;
                case "copyright":
                    config.setValue(COPYRIGHT);
                    break;
                case "head":
                    config.setValue(HEAD);
                    break;
                default:
                    break;
            }
        }
        configService.batchUpdate(configs);
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        result.setData(configs);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
