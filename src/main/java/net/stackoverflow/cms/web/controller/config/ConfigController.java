package net.stackoverflow.cms.web.controller.config;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Config;
import net.stackoverflow.cms.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/config")
@Slf4j
public class ConfigController extends BaseController {

    @Autowired
    private ConfigService configService;

    /**
     * 更新配置
     *
     * @param configs
     * @return
     */
    @RequestMapping(value = "/update")
    public ResponseEntity update(List<Config> configs) {
        Result result = new Result();
        try {
            if (configs == null || configs.size() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("参数不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
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
