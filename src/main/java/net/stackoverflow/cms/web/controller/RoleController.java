package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.IdsDTO;
import net.stackoverflow.cms.model.dto.RoleDTO;
import net.stackoverflow.cms.model.dto.TransferRoleDTO;
import net.stackoverflow.cms.service.MenuService;
import net.stackoverflow.cms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 角色管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/role")
@Slf4j
@Validated
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    /**
     * 分页查询角色信息
     *
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @param key
     * @return
     */
    @GetMapping(value = "/list")
    public ResponseEntity<Result<PageResponse<RoleDTO>>> queryPage(
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "key", required = false) String key) {
        PageResponse<RoleDTO> response = roleService.findByPage(page, limit, sort, order, key);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(response));

    }

    /**
     * 删除角色
     *
     * @param idsDTO
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Result<Object>> deleteByIds(@RequestBody @Validated IdsDTO idsDTO) {
        roleService.deleteByIds(idsDTO.getIds());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());

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
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());

    }

    /**
     * 新增角色
     *
     * @param roleDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<Result<Object>> save(@RequestBody @Validated(RoleDTO.Insert.class) RoleDTO roleDTO) {
        roleService.save(roleDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 根据id查询角色
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Result<RoleDTO>> queryRoleById(@PathVariable("id") String id) {
        RoleDTO dto = roleService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<Result<List<RoleDTO>>> queryAll() {
        List<RoleDTO> dtos = roleService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dtos));
    }

    /**
     * 用户的角色参照
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/transfer")
    public ResponseEntity<Result<TransferRoleDTO>> transfer(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
        List<RoleDTO> targetRoles = roleService.findByUserId(id);
        List<RoleDTO> allRoles = roleService.findAll();

        TransferRoleDTO dto = new TransferRoleDTO();
        dto.setAll(allRoles);
        dto.setTarget(targetRoles);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }
}