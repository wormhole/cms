package net.stackoverflow.cms.web.controller.config;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.constant.UploadConst;
import net.stackoverflow.cms.model.entity.Config;
import net.stackoverflow.cms.model.entity.File;
import net.stackoverflow.cms.model.vo.ConfigVO;
import net.stackoverflow.cms.service.ConfigService;
import net.stackoverflow.cms.service.FileService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置接口
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/config/website")
@Slf4j
@Validated
public class WebsiteController extends BaseController {

    @Autowired
    private ConfigService configService;
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
        Map<String, Object> map = new HashMap<>();
        for (Config config : configs) {
            if (config.getKey().equals("head")) {
                if (config.getValue().equals("default"))
                    map.put(config.getKey(), "/head.jpg");
                else
                    map.put(config.getKey(), UploadConst.PREFIX + config.getValue());
            } else {
                map.put(config.getKey(), config.getValue());
            }
        }
        result.setMessage("success");
        result.setStatus(Result.Status.SUCCESS);
        result.setData(map);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 更新配置
     *
     * @param configVOs
     * @return
     */
    @PutMapping(value = "/update")
    public ResponseEntity update(@RequestBody @NotEmpty(message = "参数不能为空") List<ConfigVO> configVOs) {
        Result result = new Result();

        List<Config> configs = new ArrayList<>();
        for (ConfigVO configVO : configVOs) {
            Config config = new Config();
            BeanUtils.copyProperties(configVO, config);
            configs.add(config);
        }
        configService.batchUpdateByKey(configs);
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

        File filePO = fileService.saveFile(file, getUserDetails().getId());
        Config config = new Config();
        config.setKey("head");
        config.setValue(filePO.getPath());
        configService.updateByKey(config);

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

        List<Config> configs = new ArrayList<>();

        Config title = new Config();
        title.setKey("title");
        title.setValue(TITLE);

        Config copyright = new Config();
        copyright.setKey("copyright");
        copyright.setValue(COPYRIGHT);

        Config head = new Config();
        head.setKey("head");
        head.setValue(HEAD);

        configs.add(title);
        configs.add(copyright);
        configs.add(head);

        Map<String, Object> map = new HashMap<>();
        for (Config config : configs) {
            if (config.getKey().equals("head")) {
                if (config.getValue().equals("default"))
                    map.put(config.getKey(), "/head.jpg");
                else
                    map.put(config.getKey(), UploadConst.PREFIX + config.getValue());
            } else {
                map.put(config.getKey(), config.getValue());
            }
        }

        configService.batchUpdateByKey(configs);
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        result.setData(map);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
