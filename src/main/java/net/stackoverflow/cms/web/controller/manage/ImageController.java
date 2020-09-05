package net.stackoverflow.cms.web.controller.manage;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.IdsDTO;
import net.stackoverflow.cms.model.dto.UploadDTO;
import net.stackoverflow.cms.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

/**
 * 图片管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/manage/image")
@Slf4j
@Validated
public class ImageController extends BaseController {

    @Autowired
    private UploadService uploadService;

    /**
     * 分页查询图片信息
     *
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @param key
     * @return
     */
    @GetMapping(value = "/uploads")
    public ResponseEntity<Result<PageResponse<UploadDTO>>> list(
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "key", required = false) String key) {

        PageResponse<UploadDTO> response = uploadService.findImageByPage(page, limit, sort, order, key, super.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", response));

    }

    /**
     * 删除图片
     *
     * @param idsDTO
     * @return
     */
    @DeleteMapping(value = "/uploads")
    public ResponseEntity<Result<Object>> delete(@RequestBody @Validated IdsDTO idsDTO) {
        uploadService.deleteByIds(idsDTO.getIds());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));
    }
}
