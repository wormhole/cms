package net.stackoverflow.cms.web.controller.auth;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.dto.*;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * 用户管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/auth/user")
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
     * @param page    当前页
     * @param limit   每页大小
     * @param sort    排序字段
     * @param order   排序方式
     * @param roleIds 角色过滤
     * @param key     关键字
     * @return
     */
    @GetMapping(value = "/list")
    public ResponseEntity<Result<PageResponse<UserDTO>>> queryPage(
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "roleIds[]", required = false) List<String> roleIds,
            @RequestParam(value = "key", required = false) String key) {
        PageResponse<UserDTO> response = userService.findByPage(page, limit, sort, order, key, roleIds);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(response));
    }

    /**
     * 删除用户
     *
     * @param dto 用户主键dto
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Result<Object>> deleteByIds(@RequestBody @Validated IdsDTO dto) {
        userService.deleteByIds(dto.getIds());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 更新用户信息
     *
     * @param dto 用户信息dto对象
     * @return
     */
    @PutMapping
    public ResponseEntity<Result<Object>> update(@RequestBody @Validated(UserDTO.Update.class) UserDTO dto) {
        userService.updateBase(dto);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 添加用户
     *
     * @param dto 用户信息dto对象
     * @return
     */
    @PostMapping
    public ResponseEntity<Result<Object>> save(@RequestBody @Validated(UserDTO.Insert.class) UserDTO dto) {
        userService.save(dto);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 获取用户
     *
     * @param id 用户主键
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Result<UserDTO>> queryUserById(@PathVariable("id") String id) {
        User user = userService.findById(id);
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        dto.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 启用
     *
     * @param dto 用户主键dto对象
     * @return
     */
    @PutMapping(value = "/enabled")
    public ResponseEntity<Result<Object>> enabled(@RequestBody @Validated IdsDTO dto) {
        userService.updateEnable(dto.getIds(), 1);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 禁用
     *
     * @param dto 用户主键dto对象
     * @return
     */
    @PutMapping(value = "/disabled")
    public ResponseEntity<Result<Object>> disabled(@RequestBody @Validated IdsDTO dto) {
        userService.updateEnable(dto.getIds(), 0);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 绑定角色
     *
     * @param dto 用户角色绑定信息dto对象
     * @return
     */
    @PutMapping(value = "/bind_role")
    public ResponseEntity<Result<Object>> bindRole(@RequestBody @Validated BindRoleDTO dto) {
        userService.reBindRole(dto);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 用户分配角色穿梭框数据
     *
     * @param id 用户主键
     * @return
     */
    @GetMapping(value = "/transfer")
    public ResponseEntity<Result<TransferRoleDTO>> transfer(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
        List<RoleDTO> target = roleService.findByUserId(id);
        List<RoleDTO> all = roleService.findAll();
        TransferRoleDTO dto = new TransferRoleDTO();
        dto.setAll(all);
        dto.setTarget(target);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @GetMapping(value = "/roles")
    public ResponseEntity<Result<List<RoleDTO>>> queryRoles() {
        List<RoleDTO> dtos = roleService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dtos));
    }


    /**
     * 用户注册接口
     *
     * @param dto
     * @param captcha
     * @param session
     * @return
     */
    @PostMapping(value = "/register")
    public ResponseEntity<Result<Object>> register(@RequestBody @Validated(UserDTO.Insert.class) UserDTO dto, @NotBlank(message = "验证码不能为空") @PathParam("captcha") String captcha, HttpSession session) {
        //校验验证码
        String code = (String) session.getAttribute("captcha");
        if (!captcha.equalsIgnoreCase(code)) {
            throw new BusinessException("验证码错误");
        }

        //保存至数据库
        dto.setTtl(30);
        dto.setLimit(1);
        dto.setLock(30);
        dto.setFailure(5);
        userService.save(dto);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("注册成功"));
    }
}
