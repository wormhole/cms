package net.stackoverflow.cms.web.controller.manage;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.SettingDTO;
import net.stackoverflow.cms.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 系统设置接口
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/manage/setting")
@Slf4j
@Validated
public class SettingController extends BaseController {

    @Autowired
    private SettingService settingService;

    /**
     * 获取配置信息
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<Result<SettingDTO>> getSetting() {
        SettingDTO dto = settingService.getSetting();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 更新配置
     *
     * @param dto 基础信息dto对象
     * @return
     */
    @PutMapping
    public ResponseEntity<Result<Object>> updateSetting(@RequestBody SettingDTO dto) {
        settingService.update(dto);
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
        String userId = super.getUserId();
        settingService.updateHead(userId, file);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 还原默认配置
     *
     * @return
     */
    @PutMapping(value = "/restore")
    public ResponseEntity<Result<SettingDTO>> restore() {
        SettingDTO dto = settingService.restore();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }
}
