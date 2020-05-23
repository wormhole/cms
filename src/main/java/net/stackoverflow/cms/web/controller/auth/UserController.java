package net.stackoverflow.cms.web.controller.auth;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.vo.*;
import net.stackoverflow.cms.security.CmsMd5PasswordEncoder;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.*;

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
    @Autowired
    private CmsMd5PasswordEncoder encoder;

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
    public ResponseEntity list(
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "roleIds[]", required = false) List<String> roleIds,
            @RequestParam(value = "key", required = false) String key) {

        Result result = new Result();

        Map<String, Object> resultMap = new HashMap<>(16);
        Map<String, Object> condition = new HashMap<>(16);

        //根据角色过滤
        if (roleIds != null && roleIds.size() > 0) {
            List<String> userIds = new ArrayList<>();
            userIds.add("");
            List<User> users = roleService.findUserByRoleIds(roleIds);
            if (users != null && users.size() > 0) {
                for (User user : users) {
                    userIds.add(user.getId());
                }
            }
            condition.put("ids", userIds);
        }

        if (StringUtils.isBlank(order) || StringUtils.isBlank(sort)) {
            sort = "deletable";
            order = "asc";
        }
        if (StringUtils.isBlank(key)) {
            key = null;
        }

        Page pageParam = new Page(page, limit, sort, order, condition, key);
        List<User> users = userService.findByPage(pageParam);
        pageParam.setLimit(null);
        pageParam.setOffset(null);
        int total = userService.findByPage(pageParam).size();

        List<UserVO> userVOs = new ArrayList<>();
        for (User user : users) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            userVO.setPassword(null);
            List<Role> roles = userService.findRoleByUserId(user.getId());
            List<RoleVO> roleVOs = new ArrayList<>();
            if (roles != null && roles.size() > 0) {
                for (Role role : roles) {
                    RoleVO roleVO = new RoleVO();
                    BeanUtils.copyProperties(role, roleVO);
                    roleVOs.add(roleVO);
                }
            }
            userVO.setRoles(roleVOs);
            userVOs.add(userVO);
        }

        resultMap.put("list", userVOs);
        resultMap.put("total", total);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        result.setData(resultMap);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 删除用户
     *
     * @param idsVO
     * @return
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity delete(@RequestBody @Validated IdsVO idsVO) {
        Result result = new Result();

        //检查是否有不可被删除的
        List<User> users = userService.findByIds(idsVO.getIds());
        for (User user : users) {
            if (user.getDeletable() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("超级管理员不允许被删除");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        }

        userService.batchDelete(idsVO.getIds());
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 启用
     *
     * @param idsVO
     * @return
     */
    @PutMapping(value = "/enabled")
    public ResponseEntity enabled(@RequestBody @Validated IdsVO idsVO) {
        Result result = new Result();

        List<User> users = userService.findByIds(idsVO.getIds());
        for (User user : users) {
            if (user.getDeletable() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("超级管理员不允许被操作");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            user.setEnabled(1);
        }

        //更新数据库
        userService.batchUpdate(users);
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 禁用
     *
     * @param idsVO
     * @return
     */
    @PutMapping(value = "/disabled")
    public ResponseEntity disabled(@RequestBody @Validated IdsVO idsVO) {
        Result result = new Result();

        List<User> users = userService.findByIds(idsVO.getIds());
        for (User user : users) {
            if (user.getDeletable() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("超级管理员不允许被操作");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            user.setEnabled(0);
        }

        //更新数据库
        userService.batchUpdate(users);
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 表格过滤参照
     *
     * @returnfilters
     */
    @GetMapping(value = "/ref_role")
    public ResponseEntity refRole() {
        Result result = new Result();

        List<Role> roles = roleService.findAll();
        List<RoleVO> roleVOs = new ArrayList<>();
        for (Role role : roles) {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            roleVOs.add(roleVO);
        }

        Map<String, Object> retMap = new HashMap<>(16);
        retMap.put("roles", roleVOs);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        result.setData(retMap);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 用户的角色参照
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/ref_user_role")
    public ResponseEntity refUserRole(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
        Result result = new Result();

        User user = userService.findById(id);
        if (user == null) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("不合法的id");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        List<Role> roles = userService.findRoleByUserId(id);
        List<Role> allRoles = roleService.findAll();

        List<RoleVO> roleVOs = new ArrayList<>();
        List<RoleVO> allRoleVOs = new ArrayList<>();
        for (Role role : roles) {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            roleVOs.add(roleVO);
        }
        for (Role role : allRoles) {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            allRoleVOs.add(roleVO);
        }

        Map<String, Object> retMap = new HashMap<>(16);
        retMap.put("target", roleVOs);
        retMap.put("all", allRoleVOs);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        result.setData(retMap);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 授权
     *
     * @param grantRoleVO
     * @return
     */
    @PutMapping(value = "/grant_role")
    public ResponseEntity grantRole(@RequestBody @Validated GrantRoleVO grantRoleVO) {
        Result result = new Result();

        User user = userService.findById(grantRoleVO.getUserId());
        if (user == null) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("不合法的id");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        userService.reGrantRole(grantRoleVO.getUserId(), grantRoleVO.getRoleIds());
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 更新用户信息
     *
     * @param userVO
     * @return
     */
    @PutMapping(value = "/update")
    public ResponseEntity update(@RequestBody @Validated(UserVO.Update.class) UserVO userVO) {

        Result result = new Result();

        User user = userService.findById(userVO.getId());
        if (user == null) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("不合法的id");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        if (!user.getUsername().equals(userVO.getUsername())) {
            User users = userService.findByUsername(userVO.getUsername());
            if (users != null) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("用户名已存在");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        }
        user.setUsername(userVO.getUsername());
        user.setEmail(userVO.getEmail());
        user.setTelephone(userVO.getTelephone());
        userService.update(user);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 修改密码
     *
     * @param passwordVO
     * @return
     */
    @PutMapping(value = "/password")
    public ResponseEntity password(@RequestBody @Validated(PasswordVO.Admin.class) PasswordVO passwordVO) {
        Result result = new Result();

        if (!passwordVO.getNewPassword().equals(passwordVO.getCheckPassword())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("两次密码不一致");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        User user = userService.findById(passwordVO.getId());

        String password = encoder.encode(passwordVO.getNewPassword());
        user.setPassword(password);
        userService.update(user);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("修改成功");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 添加用户
     *
     * @param userVO
     * @return
     */
    @PostMapping(value = "/add")
    public ResponseEntity add(@RequestBody @Validated(UserVO.Insert.class) UserVO userVO) {
        Result result = new Result();

        //参数校验

        User u = userService.findByUsername(userVO.getUsername());
        if (u != null) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("用户名已存在");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        String password = new CmsMd5PasswordEncoder().encode(userVO.getPassword());
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(password);
        user.setEnabled(1);
        user.setDeletable(1);
        userService.save(user);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
