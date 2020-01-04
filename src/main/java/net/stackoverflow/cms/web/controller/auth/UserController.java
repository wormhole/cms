package net.stackoverflow.cms.web.controller.auth;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.vo.GrantRoleVO;
import net.stackoverflow.cms.model.vo.IdsVO;
import net.stackoverflow.cms.model.vo.RoleVO;
import net.stackoverflow.cms.model.vo.UserVO;
import net.stackoverflow.cms.security.CmsMd5PasswordEncoder;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.JsonUtils;
import net.stackoverflow.cms.util.ValidateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 用户管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/auth/user")
@Slf4j
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
    @GetMapping(value = "/list")
    public ResponseEntity list(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "limit") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "roleIds[]", required = false) List<String> roleIds,
            @RequestParam(value = "key", required = false) String key) {

        Result result = new Result();
        try {
            //根据角色过滤
            List<String> userIds = new ArrayList<>();
            userIds.add("");
            if (roleIds != null && roleIds.size() > 0) {
                List<User> users = roleService.selectUserByRoleIds(roleIds);
                if (users != null && users.size() > 0) {
                    for (User user : users) {
                        userIds.add(user.getId());
                    }
                }
            }

            Map<String, Object> resultMap = new HashMap<>(16);
            Map<String, Object> searchMap = new HashMap<>(16);

            if (roleIds != null && roleIds.size() > 0) {
                searchMap.put("ids", userIds);
            }
            if (StringUtils.isBlank(order) || StringUtils.isBlank(sort)) {
                sort = "deletable";
                order = "asc";
            }
            if (StringUtils.isBlank(key)) {
                key = null;
            }
            Page pageParam = new Page(page, limit, sort, order, searchMap, key);
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
            log.info(JsonUtils.bean2json(result));
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 删除用户
     *
     * @param idsVO
     * @return
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity delete(@RequestBody IdsVO idsVO) {
        Result result = new Result();
        try {
            //校验参数
            if (idsVO.getIds() == null || idsVO.getIds().size() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("请至少选择一条数据");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

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
            result.setMessage("删除成功");
            log.info(JsonUtils.bean2json(result));
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 启用
     *
     * @param idsVO
     * @return
     */
    @PutMapping(value = "/enabled")
    public ResponseEntity enabled(@RequestBody IdsVO idsVO) {
        Result result = new Result();
        try {
            //校验参数
            if (idsVO.getIds() == null || idsVO.getIds().size() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("请至少选择一条数据");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

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
            result.setMessage("启用成功");
            log.info(JsonUtils.bean2json(result));
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 禁用
     *
     * @param idsVO
     * @return
     */
    @PutMapping(value = "/disabled")
    public ResponseEntity disabled(@RequestBody IdsVO idsVO) {
        Result result = new Result();
        try {
            //校验参数
            if (idsVO.getIds() == null || idsVO.getIds().size() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("请至少选择一条数据");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

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
            result.setMessage("禁用成功");
            log.info(JsonUtils.bean2json(result));
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 表格过滤参照
     *
     * @returnfilters
     */
    @GetMapping(value = "/filters")
    public ResponseEntity filters() {
        Result result = new Result();
        try {
            List<Role> roles = roleService.selectByCondition(new HashMap<String, Object>(16));
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
            log.info(JsonUtils.bean2json(result));
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 用户的角色参照
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/ref_user_role")
    public ResponseEntity refUserRole(@RequestParam(value = "id") String id) {
        Result result = new Result();
        try {
            //校验参数
            if (StringUtils.isBlank(id)) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("id不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            User user = userService.findById(id);
            if (user == null) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("不合法的id");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            List<Role> roles = userService.findRoleByUserId(id);
            List<Role> allRoles = roleService.selectByCondition(new HashMap<String, Object>(16));

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
            log.info(JsonUtils.bean2json(result));
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 授权
     *
     * @param grantRoleVO
     * @return
     */
    @PutMapping(value = "/grant_role")
    public ResponseEntity grantRole(@RequestBody GrantRoleVO grantRoleVO) {
        Result result = new Result();
        try {
            //校验数据
            if (StringUtils.isBlank(grantRoleVO.getUserId())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("userId不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            User user = userService.findById(grantRoleVO.getUserId());
            if (user == null) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("不合法的id");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            userService.reGrantRole(grantRoleVO.getUserId(), grantRoleVO.getRoleIds());
            result.setStatus(Result.Status.SUCCESS);
            result.setMessage("授权成功");
            log.info(JsonUtils.bean2json(result));
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 更新用户信息
     *
     * @param userVO
     * @return
     */
    @PutMapping(value = "/update")
    public ResponseEntity update(@RequestBody UserVO userVO) {

        Result result = new Result();
        try {
            //校验参数
            if (userVO.getType() != 0 && userVO.getType() != 1) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("类型错误");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            User user = userService.findById(userVO.getId());
            if (user == null) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("不合法的id");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            if (userVO.getType() == 0) {
                if (!ValidateUtils.validateUsername(userVO.getUsername())) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("用户名不能为空");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                } else {
                    if (!user.getUsername().equals(userVO.getUsername())) {
                        List<User> users = userService.findByCondition(new HashMap<String, Object>(16) {{
                            put("username", userVO.getUsername());
                        }});
                        if (users != null && users.size() > 0) {
                            result.setStatus(Result.Status.FAILURE);
                            result.setMessage("用户名已存在");
                            return ResponseEntity.status(HttpStatus.OK).body(result);
                        }
                    }
                }
                if (!ValidateUtils.validateEmail(userVO.getEmail())) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("邮箱格式错误");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
                if (!ValidateUtils.validateTelephone(userVO.getTelephone())) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("电话号码格式错误");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
                user.setUsername(userVO.getUsername());
                user.setEmail(userVO.getEmail());
                user.setTelephone(userVO.getTelephone());
                userService.update(user);
            } else if (userVO.getType() == 1) {
                if (!ValidateUtils.validatePassword(userVO.getPassword())) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("密码长度不能小于6");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
                String password = new CmsMd5PasswordEncoder().encode(userVO.getPassword());
                user.setPassword(password);
                userService.update(user);
            }
            result.setStatus(Result.Status.SUCCESS);
            result.setMessage("更新成功");
            log.info(JsonUtils.bean2json(result));
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 添加用户
     *
     * @param userVO
     * @return
     */
    @PostMapping(value = "/add")
    public ResponseEntity add(@RequestBody UserVO userVO) {
        Result result = new Result();
        try {
            //参数校验
            if (!ValidateUtils.validateUsername(userVO.getUsername())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("用户名不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                List<User> users = userService.findByCondition(new HashMap<String, Object>(16) {{
                    put("username", userVO.getUsername());
                }});
                if (users != null && users.size() > 0) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("用户名已存在");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
            }
            if (!ValidateUtils.validateEmail(userVO.getEmail())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("邮箱格式错误");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            if (!ValidateUtils.validateTelephone(userVO.getTelephone())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("电话号码格式错误");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            if (!ValidateUtils.validatePassword(userVO.getPassword())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("密码长度不能小于6");
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
            result.setMessage("添加成功");
            log.info(JsonUtils.bean2json(result));
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
