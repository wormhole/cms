package net.stackoverflow.cms.web.controller.auth;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.IdsDTO;
import net.stackoverflow.cms.model.dto.MenuDTO;
import net.stackoverflow.cms.model.dto.RoleDTO;
import net.stackoverflow.cms.service.MenuService;
import net.stackoverflow.cms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

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
    private MenuService menuService;

    /**
     * 分页查询角色信息
     *
     * @param page  当前页
     * @param limit 每页大小
     * @param sort  排序字段
     * @param order 排序顺序
     * @param key   关键字
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
     * @param dto 角色主键列表
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Result<Object>> deleteByIds(@RequestBody @Validated IdsDTO dto) {
        roleService.deleteByIds(dto.getIds());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());

    }

    /**
     * 更新角色信息
     *
     * @param dto 角色信息dto对象
     * @return
     */
    @PutMapping
    public ResponseEntity<Result<Object>> update(@RequestBody @Validated(RoleDTO.Update.class) RoleDTO dto) {
        roleService.update(dto);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());

    }

    /**
     * 新增角色
     *
     * @param dto 角色信息dto对象
     * @return
     */
    @PostMapping
    public ResponseEntity<Result<Object>> save(@RequestBody @Validated(RoleDTO.Insert.class) RoleDTO dto) {
        roleService.save(dto);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 根据id查询角色
     *
     * @param id 角色主键
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Result<RoleDTO>> queryRoleById(@PathVariable("id") String id) {
        RoleDTO dto = roleService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 获取菜单树
     *
     * @return
     */
    @GetMapping("/menu_tree")
    public ResponseEntity<Result<List<MenuDTO>>> queryMenuTree() {
        List<MenuDTO> dtos = menuService.findTree();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dtos));
    }
}