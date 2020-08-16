package net.stackoverflow.cms.web.controller.auth;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.IdsDTO;
import net.stackoverflow.cms.model.dto.PermissionDTO;
import net.stackoverflow.cms.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

/**
 * 权限管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/auth/permission_manage")
@Slf4j
@Validated
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 分页查询权限信息
     *
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @param key
     * @return
     */
    @GetMapping(value = "/permissions")
    public ResponseEntity<Result<PageResponse<PermissionDTO>>> list(
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "key", required = false) String key) {

        PageResponse<PermissionDTO> response = permissionService.findByPage(page, limit, sort, order, key);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", response));

    }

    /**
     * 删除权限
     *
     * @param idsDTO
     * @return
     */
    @DeleteMapping(value = "/permissions")
    public ResponseEntity<Result<Object>> delete(@RequestBody @Validated IdsDTO idsDTO) {
        permissionService.deleteByIds(idsDTO.getIds());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));

    }

    /**
     * 更新权限信息
     *
     * @param permissionDTO
     * @return
     */
    @PutMapping(value = "/permission")
    public ResponseEntity<Result<Object>> update(@RequestBody @Validated(PermissionDTO.Update.class) PermissionDTO permissionDTO) {
        permissionService.update(permissionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));
    }

    /**
     * 新增权限
     *
     * @param permissionDTO
     * @return
     */
    @PostMapping(value = "/permission")
    public ResponseEntity<Result<Object>> add(@RequestBody @Validated(PermissionDTO.Insert.class) PermissionDTO permissionDTO) {
        permissionService.save(permissionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));

    }
}
