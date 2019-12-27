package net.stackoverflow.cms.web.controller.auth;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.vo.GrantPermissionVO;
import net.stackoverflow.cms.model.vo.IdsVO;
import net.stackoverflow.cms.model.vo.PermissionVO;
import net.stackoverflow.cms.model.vo.RoleVO;
import net.stackoverflow.cms.service.PermissionService;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.util.JsonUtils;
import net.stackoverflow.cms.util.ValidateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 角色管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/auth/role")
@Slf4j
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
    public ResponseEntity list(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "limit") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "permissionIds[]", required = false) List<String> permissionIds,
            @RequestParam(value = "key", required = false) String key) {

        Result result = new Result();
        try {
            //根据权限过滤
            List<String> roleIds = new ArrayList<>();
            roleIds.add("");
            if (permissionIds != null && permissionIds.size() > 0) {
                List<Role> roles = permissionService.selectRoleByPermissionIds(permissionIds);
                if (roles != null && roles.size() > 0) {
                    for (Role role : roles) {
                        roleIds.add(role.getId());
                    }
                }
            }

            Map<String, Object> resultMap = new HashMap<>(16);
            Map<String, Object> searchMap = new HashMap<>(16);
            int total = 0;

            if (permissionIds != null && permissionIds.size() > 0) {
                searchMap.put("ids", roleIds);
            }
            if (StringUtils.isBlank(order) || StringUtils.isBlank(sort)) {
                sort = "deletable";
                order = "asc";
            }
            if (StringUtils.isBlank(key)) {
                key = null;
            }
            Page pageParam = new Page(page, limit, sort, order, searchMap, key);
            List<Role> roles = roleService.selectByPage(pageParam);
            pageParam.setLimit(null);
            pageParam.setOffset(null);
            total = roleService.selectByPage(pageParam).size();

            List<RoleVO> roleVOs = new ArrayList<>();
            for (Role role : roles) {
                RoleVO roleVO = new RoleVO();
                BeanUtils.copyProperties(role, roleVO);
                List<Permission> permissions = roleService.getPermissionByRoleId(role.getId());
                List<PermissionVO> permissionVOs = new ArrayList<>();
                if (permissions != null && permissions.size() > 0) {
                    for (Permission permission : permissions) {
                        PermissionVO permissionVO = new PermissionVO();
                        BeanUtils.copyProperties(permission, permissionVO);
                        permissionVOs.add(permissionVO);
                    }
                }
                roleVO.setPermissions(permissionVOs);
                roleVOs.add(roleVO);
            }

            resultMap.put("list", roleVOs);
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
     * 表格过滤参照
     *
     * @returnfilters
     */
    @GetMapping(value = "/filters")
    public ResponseEntity filters() {
        Result result = new Result();
        try {
            List<Permission> permissions = permissionService.selectByCondition(new HashMap<String, Object>(16));
            List<PermissionVO> permissionVOs = new ArrayList<>();
            for (Permission permission : permissions) {
                PermissionVO permissionVO = new PermissionVO();
                BeanUtils.copyProperties(permission, permissionVO);
                permissionVOs.add(permissionVO);
            }

            Map<String, Object> retMap = new HashMap<>(16);
            retMap.put("permissions", permissionVOs);

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
     * 删除角色
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
            Map<String, Object> searchMap = new HashMap<>(16);
            searchMap.put("ids", idsVO.getIds());
            List<Role> roles = roleService.selectByCondition(searchMap);
            for (Role role : roles) {
                if (role.getDeletable() == 0) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("包含不允许被删除的角色");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
            }

            roleService.batchDelete(idsVO.getIds());
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
     * 角色的权限参照
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/ref_role_permission")
    public ResponseEntity refRolePermission(@RequestParam(value = "id") String id) {
        Result result = new Result();
        try {
            //校验参数
            if (StringUtils.isBlank(id)) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("id不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            Role role = roleService.select(id);
            if (role == null) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("不合法的id");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            List<Permission> permissions = roleService.getPermissionByRoleId(role.getId());
            List<Permission> allPermissions = permissionService.selectByCondition(new HashMap<String, Object>(16));

            List<PermissionVO> permissionVOs = new ArrayList<>();
            List<PermissionVO> allPermissionVOs = new ArrayList<>();
            for (Permission permission : permissions) {
                PermissionVO permissionVO = new PermissionVO();
                BeanUtils.copyProperties(permission, permissionVO);
                permissionVOs.add(permissionVO);
            }
            for (Permission permission : allPermissions) {
                PermissionVO permissionVO = new PermissionVO();
                BeanUtils.copyProperties(permission, permissionVO);
                allPermissionVOs.add(permissionVO);
            }

            Map<String, Object> retMap = new HashMap<>(16);
            retMap.put("target", permissionVOs);
            retMap.put("all", allPermissionVOs);

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
     * @param grantPermissionVO
     * @return
     */
    @PutMapping(value = "/grant_permission")
    public ResponseEntity grantPermission(@RequestBody GrantPermissionVO grantPermissionVO) {
        Result result = new Result();
        try {
            //校验数据
            if (StringUtils.isBlank(grantPermissionVO.getRoleId())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("roleId不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            Role role = roleService.select(grantPermissionVO.getRoleId());
            if (role == null) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("不合法的id");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else if (role.getDeletable() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("该角色不允许被操作");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            roleService.reGrantPermission(grantPermissionVO.getRoleId(), grantPermissionVO.getPermissionIds());
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
     * 更新角色信息
     *
     * @param roleVO
     * @return
     */
    @PutMapping(value = "/update")
    public ResponseEntity update(@RequestBody RoleVO roleVO) {

        Result result = new Result();
        try {
            //校验参数
            Role role = roleService.select(roleVO.getId());
            if (role == null) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("不合法的id");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else if (role.getDeletable() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("该角色不允许被操作");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            if (!ValidateUtils.validateName(roleVO.getName())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("名称不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                if (!role.getName().equals(roleVO.getName())) {
                    List<Role> roles = roleService.selectByCondition(new HashMap<String, Object>(16) {{
                        put("name", roleVO.getName());
                    }});
                    if (roles != null && roles.size() > 0) {
                        result.setStatus(Result.Status.FAILURE);
                        result.setMessage("角色名已存在");
                        return ResponseEntity.status(HttpStatus.OK).body(result);
                    }
                }
            }

            role.setName(roleVO.getName());
            role.setDescription(roleVO.getDescription());
            roleService.update(role);

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
     * 新增角色
     *
     * @param roleVO
     * @return
     */
    @PostMapping(value = "/add")
    public ResponseEntity add(@RequestBody RoleVO roleVO) {
        Result result = new Result();
        try {
            //校验参数
            if (!ValidateUtils.validateName(roleVO.getName())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("名称不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                List<Role> roles = roleService.selectByCondition(new HashMap<String, Object>(16) {{
                    put("name", roleVO.getName());
                }});
                if (roles != null && roles.size() > 0) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("角色名已存在");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
            }

            Role role = new Role();
            BeanUtils.copyProperties(roleVO, role);
            role.setId(UUID.randomUUID().toString());
            role.setDeletable(1);
            roleService.insert(role);

            //默认授予控制面板权限
            List<Permission> permissions = permissionService.selectByCondition(new HashMap<String, Object>(16) {{
                put("name", "dashboard");
            }});
            if (permissions != null && permissions.size() > 0) {
                Permission permission = permissions.get(0);
                roleService.grantPermission(role.getId(), permission.getId());
            }

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