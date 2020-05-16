package net.stackoverflow.cms.web.controller.auth;

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
    public ResponseEntity list(
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "permissionIds[]", required = false) List<String> permissionIds,
            @RequestParam(value = "key", required = false) String key) {

        Result result = new Result();

        Map<String, Object> resultMap = new HashMap<>(16);
        Map<String, Object> condition = new HashMap<>(16);

        //根据权限过滤
        if (permissionIds != null && permissionIds.size() > 0) {
            List<String> roleIds = new ArrayList<>();
            roleIds.add("");
            List<Role> roles = permissionService.findRoleByPermissionIds(permissionIds);
            if (roles != null && roles.size() > 0) {
                for (Role role : roles) {
                    roleIds.add(role.getId());
                }
            }
            condition.put("ids", roleIds);
        }

        if (StringUtils.isBlank(order) || StringUtils.isBlank(sort)) {
            sort = "deletable";
            order = "asc";
        }
        if (StringUtils.isBlank(key)) {
            key = null;
        }

        Page pageParam = new Page(page, limit, sort, order, condition, key);
        List<Role> roles = roleService.findByPage(pageParam);
        pageParam.setLimit(null);
        pageParam.setOffset(null);
        int total = roleService.findByPage(pageParam).size();

        List<RoleVO> roleVOs = new ArrayList<>();
        for (Role role : roles) {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            List<Permission> permissions = roleService.findPermissionByRoleId(role.getId());
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
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 表格过滤参照
     *
     * @returnfilters
     */
    @GetMapping(value = "/ref_permission")
    public ResponseEntity refPermission() {
        Result result = new Result();

        List<Permission> permissions = permissionService.findAll();
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
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 删除角色
     *
     * @param idsVO
     * @return
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity delete(@RequestBody @Validated IdsVO idsVO) {
        Result result = new Result();

        //检查是否有不可被删除的
        List<Role> roles = roleService.findByIds(idsVO.getIds());
        for (Role role : roles) {
            if (role.getDeletable() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("包含不允许被删除的角色");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        }

        roleService.batchDelete(idsVO.getIds());
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 角色的权限参照
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/ref_role_permission")
    public ResponseEntity refRolePermission(@RequestParam(value = "id") @NotBlank(message = "id不能为空") String id) {
        Result result = new Result();

        Role role = roleService.findById(id);
        if (role == null) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("不合法的id");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        List<Permission> permissions = roleService.findPermissionByRoleId(role.getId());
        List<Permission> allPermissions = permissionService.findAll();

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
        return ResponseEntity.status(HttpStatus.OK).body(result);

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

        //校验数据
        if (StringUtils.isBlank(grantPermissionVO.getRoleId())) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("roleId不能为空");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        Role role = roleService.findById(grantPermissionVO.getRoleId());
        if (role == null) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("不合法的id");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        roleService.reGrantPermission(grantPermissionVO.getRoleId(), grantPermissionVO.getPermissionIds());
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 更新角色信息
     *
     * @param roleVO
     * @return
     */
    @PutMapping(value = "/update")
    public ResponseEntity update(@RequestBody @Validated(RoleVO.Update.class) RoleVO roleVO) {

        Result result = new Result();

        //校验参数
        Role role = roleService.findById(roleVO.getId());
        if (role == null) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("不合法的id");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        if (!role.getName().equals(roleVO.getName())) {
            Role roles = roleService.findByName(roleVO.getName());
            if (roles != null) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("角色名已存在");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        }

        role.setName(roleVO.getName());
        role.setDescription(roleVO.getDescription());
        roleService.update(role);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 新增角色
     *
     * @param roleVO
     * @return
     */
    @PostMapping(value = "/add")
    public ResponseEntity add(@RequestBody @Validated(RoleVO.Insert.class) RoleVO roleVO) {
        Result result = new Result();

        Role r = roleService.findByName(roleVO.getName());
        if (r != null) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("角色名已存在");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleVO, role);
        role.setId(UUID.randomUUID().toString());
        role.setDeletable(1);
        roleService.save(role);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}