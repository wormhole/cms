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
@RequestMapping(value = "/auth/role_manage")
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
    @GetMapping(value = "/roles")
    public ResponseEntity<Result<PageResponse<RoleDTO>>> list(
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "key", required = false) String key) {
        PageResponse<RoleDTO> response = roleService.findByPage(page, limit, sort, order, key);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", response));

    }

    /**
     * 删除角色
     *
     * @param idsDTO
     * @return
     */
    @DeleteMapping(value = "/roles")
    public ResponseEntity<Result<Object>> delete(@RequestBody @Validated IdsDTO idsDTO) {
        roleService.deleteByIds(idsDTO.getIds());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));

    }

    /**
     * 更新角色信息
     *
     * @param roleDTO
     * @return
     */
    @PutMapping(value = "/role")
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
    @PostMapping(value = "/role")
    public ResponseEntity<Result<Object>> add(@RequestBody @Validated(RoleDTO.Insert.class) RoleDTO roleDTO) {
        roleService.save(roleDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));
    }

    /**
     * 根据id查询角色
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/role/{id}")
    public ResponseEntity<Result<RoleDTO>> get(@PathVariable("id") String id) {
        RoleDTO dto = roleService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", dto));
    }

    /**
     * 获取菜单树
     *
     * @return
     */
    @GetMapping(value = "/menu")
    public ResponseEntity<Result<List<MenuDTO>>> menu() {
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", menuService.getAll()));
    }
}