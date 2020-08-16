package net.stackoverflow.cms.web.controller.auth;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.dto.*;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.service.UserService;
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
 * 用户管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/auth/user_manage")
@Slf4j
@Validated
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 分页查询用户信息
     *
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @param roleIds
     * @param key
     * @return
     */
    @GetMapping(value = "/users")
    public ResponseEntity<Result<PageResponse<UserDTO>>> list(
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "roleIds[]", required = false) List<String> roleIds,
            @RequestParam(value = "key", required = false) String key) {
        PageResponse<UserDTO> response = userService.findByPage(page, limit, sort, order, key, roleIds);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", response));
    }

    /**
     * 删除用户
     *
     * @param idsDTO
     * @return
     */
    @DeleteMapping(value = "/users")
    public ResponseEntity<Result<Object>> delete(@RequestBody @Validated IdsDTO idsDTO) {
        userService.deleteByIds(idsDTO.getIds());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));
    }

    /**
     * 启用
     *
     * @param idsDTO
     * @return
     */
    @PutMapping(value = "/user/enabled")
    public ResponseEntity<Result<Object>> enabled(@RequestBody @Validated IdsDTO idsDTO) {
        userService.updateEnable(idsDTO.getIds(), 1);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));

    }

    /**
     * 禁用
     *
     * @param idsDTO
     * @return
     */
    @PutMapping(value = "/user/disabled")
    public ResponseEntity<Result<Object>> disabled(@RequestBody @Validated IdsDTO idsDTO) {
        userService.updateEnable(idsDTO.getIds(), 0);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));

    }

    /**
     * 表格过滤参照
     *
     * @returnfilters
     */
    @GetMapping(value = "/ref_role")
    public ResponseEntity<Result<List<RoleDTO>>> refRole() {
        List<RoleDTO> roleDTOS = roleService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", roleDTOS));

    }

    /**
     * 用户的角色参照
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/ref_user_role")
    public ResponseEntity<Result<Map<String, List<RoleDTO>>>> refUserRole(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
        List<RoleDTO> targetRoles = userService.findRoleByUserId(id);
        List<RoleDTO> allRoles = roleService.findAll();

        Map<String, List<RoleDTO>> retMap = new HashMap<>(16);
        retMap.put("target", targetRoles);
        retMap.put("all", allRoles);

        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", retMap));

    }

    /**
     * 授权
     *
     * @param grantRoleDTO
     * @return
     */
    @PutMapping(value = "/grant_role")
    public ResponseEntity<Result<Object>> grantRole(@RequestBody @Validated GrantRoleDTO grantRoleDTO) {
        userService.reGrandRole(grantRoleDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));

    }

    /**
     * 更新用户信息
     *
     * @param userDTO
     * @return
     */
    @PutMapping(value = "/user")
    public ResponseEntity<Result<Object>> update(@RequestBody @Validated(UserDTO.Update.class) UserDTO userDTO) {

        userService.updateBase(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));
    }

    /**
     * 修改密码
     *
     * @param passwordDTO
     * @return
     */
    @PutMapping(value = "/user/password")
    public ResponseEntity<Result<Object>> password(@RequestBody @Validated(PasswordDTO.Admin.class) PasswordDTO passwordDTO) {

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getCheckPassword())) {
            throw new BusinessException("两次密码不一致");
        }

        userService.updatePassword(passwordDTO.getId(), passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));
    }

    /**
     * 添加用户
     *
     * @param userDTO
     * @return
     */
    @PostMapping(value = "/user")
    public ResponseEntity<Result<Object>> add(@RequestBody @Validated(UserDTO.Insert.class) UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success"));
    }
}
