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
     * @param page  当前页
     * @param limit 每页大小
     * @param sort  排序字段
     * @param order 排序方式
     * @param key   关键字
     * @return
     */
    @GetMapping(value = "/list")
    public ResponseEntity<Result<PageResponse<UploadDTO>>> queryPage(
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "key", required = false) String key) {

        PageResponse<UploadDTO> response = uploadService.findImageByPage(page, limit, sort, order, key, super.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(response));
    }

    /**
     * 删除图片
     *
     * @param dto 图片主键dto对象
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Result<Object>> deleteByIds(@RequestBody @Validated IdsDTO dto) {
        uploadService.deleteByIds(dto.getIds());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }
}
