package net.stackoverflow.cms.web.controller.config;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Config;
import net.stackoverflow.cms.model.vo.ConfigVO;
import net.stackoverflow.cms.service.ConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/config")
@Slf4j
public class ConfigController extends BaseController {

    @Autowired
    private ConfigService configService;

    /**
     * 获取配置信息
     *
     * @return
     */
    @GetMapping(value = "/info")
    public ResponseEntity config() {
        Result result = new Result();
        try {
            List<Config> configs = configService.findAll();
            List<ConfigVO> configVOs = new ArrayList<>();
            for (Config config : configs) {
                ConfigVO configVO = new ConfigVO();
                BeanUtils.copyProperties(config, configVO);
                configVOs.add(configVO);
            }
            result.setMessage("success");
            result.setStatus(Result.Status.SUCCESS);
            result.setData(configVOs);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
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
        try {
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
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
