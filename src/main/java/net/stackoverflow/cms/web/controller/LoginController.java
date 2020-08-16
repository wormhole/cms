package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.PropertyDTO;
import net.stackoverflow.cms.service.PropertyService;
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
    private PropertyService propertyService;

    /**
     * 是否开启记住我功能
     *
     * @return
     */
    @GetMapping("/property/remember")
    public ResponseEntity<Result<String>> isRememberMe() {
        PropertyDTO propertyDTO = propertyService.findByKey("rememberMe");
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", propertyDTO.getValue()));
    }
}
