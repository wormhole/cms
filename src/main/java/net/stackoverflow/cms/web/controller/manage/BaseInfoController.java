package net.stackoverflow.cms.web.controller.manage;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.BaseInfoDTO;
import net.stackoverflow.cms.service.BaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 基础信息接口
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/manage/base")
@Slf4j
@Validated
public class BaseInfoController extends BaseController {

    @Autowired
    private BaseInfoService baseInfoService;

    /**
     * 获取配置信息
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<Result<BaseInfoDTO>> queryBaseInfo() {
        BaseInfoDTO dto = baseInfoService.queryBaseInfo();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 更新配置
     *
     * @param dto 基础信息dto对象
     * @return
     */
    @PutMapping
    public ResponseEntity<Result<Object>> update(@RequestBody BaseInfoDTO dto) {
        baseInfoService.updateBaseInfo(dto);
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
        baseInfoService.updateHead(userId, file);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 还原默认配置
     *
     * @return
     */
    @PutMapping(value = "/restore")
    public ResponseEntity<Result<BaseInfoDTO>> restore() {
        BaseInfoDTO dto = baseInfoService.restore();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }
}
