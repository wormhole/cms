package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Config;
import net.stackoverflow.cms.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录Controller
 *
 * @author 凉衫薄
 */
@RestController
@Slf4j
@RequestMapping(value = "/login")
public class LoginController extends BaseController {

    @Autowired
    private ConfigService configService;

    /**
     * 是否开启记住我功能
     *
     * @return
     */
    @GetMapping("/remember")
    public ResponseEntity isRememberMe() {
        Result result = new Result();

        Config config = configService.findByKey("rememberMe");

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        result.setData(config.getValue());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
