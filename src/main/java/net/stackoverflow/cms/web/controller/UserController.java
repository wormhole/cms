package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.dto.GrantRoleDTO;
import net.stackoverflow.cms.model.dto.IdsDTO;
import net.stackoverflow.cms.model.dto.PasswordDTO;
import net.stackoverflow.cms.model.dto.UserDTO;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 用户管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/user")
@Slf4j
@Validated
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

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
     * @param idsDTO
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Result<Object>> deleteByIds(@RequestBody @Validated IdsDTO idsDTO) {
        userService.deleteByIds(idsDTO.getIds());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 启用
     *
     * @param idsDTO
     * @return
     */
    @PutMapping(value = "/enabled")
    public ResponseEntity<Result<Object>> enabled(@RequestBody @Validated IdsDTO idsDTO) {
        userService.updateEnable(idsDTO.getIds(), 1);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());

    }

    /**
     * 禁用
     *
     * @param idsDTO
     * @return
     */
    @PutMapping(value = "/disabled")
    public ResponseEntity<Result<Object>> disabled(@RequestBody @Validated IdsDTO idsDTO) {
        userService.updateEnable(idsDTO.getIds(), 0);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());

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
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());

    }

    /**
     * 更新用户信息
     *
     * @param userDTO
     * @return
     */
    @PutMapping
    public ResponseEntity<Result<Object>> updateBase(@RequestBody @Validated(UserDTO.Update.class) UserDTO userDTO) {
        User user = super.getUser();
        if (!user.getBuiltin().equals(1) && !user.getId().equals(userDTO.getId())) {
            throw new BusinessException("非超级管理员不允许更新别人信息");
        }
        userService.updateBase(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 修改密码
     *
     * @param dto
     * @return
     */
    @PutMapping(value = "/password")
    public ResponseEntity<Result<Object>> updatePassword(@RequestBody @Validated PasswordDTO dto) {
        userService.updatePassword(super.getUserId(), dto.getOld(), dto.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 添加用户
     *
     * @param userDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<Result<Object>> save(@RequestBody @Validated(UserDTO.Insert.class) UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 获取用户
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Result<UserDTO>> queryUserById(@PathVariable(value = "id", required = false) String id) {
        if (StringUtils.isEmpty(id)) {
            id = super.getUserId();
        }
        User user = userService.findById(id);
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        dto.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 用户注册接口
     *
     * @param dto
     * @param session
     * @return
     */
    @PostMapping(value = "/register")
    public ResponseEntity<Result<Object>> register(@RequestBody @Validated(UserDTO.Insert.class) UserDTO dto, HttpSession session) {
        //校验验证码
        String captcha = (String) session.getAttribute("captcha");
        if (!captcha.equalsIgnoreCase(dto.getCaptcha())) {
            throw new BusinessException("验证码错误");
        }

        //保存至数据库
        userService.save(dto);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success("注册成功"));
    }
}
