package net.stackoverflow.cms.web.controller.auth;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.vo.PermissionVO;
import net.stackoverflow.cms.model.vo.RoleVO;
import net.stackoverflow.cms.service.PermissionService;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private UserService userService;
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
}
