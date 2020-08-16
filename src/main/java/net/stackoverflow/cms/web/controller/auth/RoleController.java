package net.stackoverflow.cms.web.controller.auth;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.GrantPermissionDTO;
import net.stackoverflow.cms.model.dto.IdsDTO;
import net.stackoverflow.cms.model.dto.PermissionDTO;
import net.stackoverflow.cms.model.dto.RoleDTO;
import net.stackoverflow.cms.service.PermissionService;
import net.stackoverflow.cms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/auth/role")
@Slf4j
@Validated
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 分页查询角色信息
     *
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @param permissionIds
     * @param key
     * @return
     */
    @GetMapping(value = "/list")
    public ResponseEntity<Result<PageResponse<RoleDTO>>> list(
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "permissionIds[]", required = false) List<String> permissionIds,
            @RequestParam(value = "key", required = false) String key) {
        PageResponse<RoleDTO> response = roleService.findByPage(page, limit, sort, order, key, permissionIds);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", response));

    }

    /**
     * 表格过滤参照
     *
     * @returnfilters
     */
    @GetMapping(value = "/ref_permission")
    public ResponseEntity<Result<List<PermissionDTO>>> refPermission() {
        List<PermissionDTO> permissionDTOS = permissionService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", permissionDTOS));

    }

    /**
     * 删除角色
     *
     * @param idsDTO
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Result<Object>> delete(@RequestBody @Validated IdsDTO idsDTO) {
        roleService.deleteByIds(idsDTO.getIds());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));

    }

    /**
     * 角色的权限参照
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/ref_role_permission")
    public ResponseEntity<Result<Map<String, List<PermissionDTO>>>> refRolePermission(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {

        List<PermissionDTO> targetPermissions = roleService.findPermissionByRoleId(id);
        List<PermissionDTO> allPermissions = permissionService.findAll();

        Map<String, List<PermissionDTO>> retMap = new HashMap<>(16);
        retMap.put("target", targetPermissions);
        retMap.put("all", allPermissions);

        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", retMap));

    }

    /**
     * 授权
     *
     * @param grantPermissionDTO
     * @return
     */
    @PutMapping(value = "/grant_permission")
    public ResponseEntity<Result<Object>> grantPermission(@RequestBody @Validated GrantPermissionDTO grantPermissionDTO) {

        roleService.reGrantPermission(grantPermissionDTO.getRoleId(), grantPermissionDTO.getPermissionIds());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));

    }

    /**
     * 更新角色信息
     *
     * @param roleDTO
     * @return
     */
    @PutMapping
    public ResponseEntity<Result<Object>> update(@RequestBody @Validated(RoleDTO.Update.class) RoleDTO roleDTO) {

        roleService.update(roleDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));

    }

    /**
     * 新增角色
     *
     * @param roleDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<Result<Object>> add(@RequestBody @Validated(RoleDTO.Insert.class) RoleDTO roleDTO) {
        roleService.save(roleDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));
    }
}