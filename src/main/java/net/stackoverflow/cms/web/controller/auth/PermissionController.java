package net.stackoverflow.cms.web.controller.auth;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.vo.IdsVO;
import net.stackoverflow.cms.model.vo.PermissionVO;
import net.stackoverflow.cms.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 权限管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/auth/permission")
@Slf4j
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 分页查询权限信息
     *
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @param key
     * @return
     */
    @GetMapping(value = "/list")
    public ResponseEntity list(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "limit") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "key", required = false) String key) {

        Result result = new Result();
        try {
            Map<String, Object> resultMap = new HashMap<>(16);
            Map<String, Object> condition = new HashMap<>(16);

            if (StringUtils.isBlank(order) || StringUtils.isBlank(sort)) {
                sort = "deletable";
                order = "asc";
            }
            if (StringUtils.isBlank(key)) {
                key = null;
            }
            Page pageParam = new Page(page, limit, sort, order, condition, key);
            List<Permission> permissions = permissionService.findByPage(pageParam);
            pageParam.setLimit(null);
            pageParam.setOffset(null);
            int total = permissionService.findByPage(pageParam).size();

            List<PermissionVO> permissionVOs = new ArrayList<>();
            for (Permission permission : permissions) {
                PermissionVO permissionVO = new PermissionVO();
                BeanUtils.copyProperties(permission, permissionVO);
                permissionVOs.add(permissionVO);
            }

            resultMap.put("list", permissionVOs);
            resultMap.put("total", total);

            result.setStatus(Result.Status.SUCCESS);
            result.setMessage("success");
            result.setData(resultMap);
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 删除权限
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
            List<Permission> permissions = permissionService.findByIds(idsVO.getIds());
            for (Permission permission : permissions) {
                if (permission.getDeletable() == 0) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("包含不允许被删除的权限");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
            }

            permissionService.batchDelete(idsVO.getIds());
            result.setStatus(Result.Status.SUCCESS);
            result.setMessage("success");
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 更新权限信息
     *
     * @param permissionVO
     * @return
     */
    @PutMapping(value = "/update")
    public ResponseEntity update(@RequestBody PermissionVO permissionVO) {

        Result result = new Result();
        try {
            //校验参数
            Permission permission = permissionService.findById(permissionVO.getId());
            if (permission == null) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("不合法的id");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else if (permission.getDeletable() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("该权限不允许被操作");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            if (StringUtils.isBlank(permissionVO.getName())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("名称不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                if (!permission.getName().equals(permissionVO.getName())) {
                    List<Permission> permissions = permissionService.findByCondition(new HashMap<String, Object>(16) {{
                        put("name", permissionVO.getName());
                    }});
                    if (permissions != null && permissions.size() > 0) {
                        result.setStatus(Result.Status.FAILURE);
                        result.setMessage("权限名已存在");
                        return ResponseEntity.status(HttpStatus.OK).body(result);
                    }
                }
            }

            permission.setName(permissionVO.getName());
            permission.setDescription(permissionVO.getDescription());
            permissionService.update(permission);

            result.setStatus(Result.Status.SUCCESS);
            result.setMessage("success");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 新增权限
     *
     * @param permissionVO
     * @return
     */
    @PostMapping(value = "/add")
    public ResponseEntity add(@RequestBody PermissionVO permissionVO) {
        Result result = new Result();
        try {
            //校验参数
            if (StringUtils.isBlank(permissionVO.getName())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("名称不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                List<Permission> permissions = permissionService.findByCondition(new HashMap<String, Object>(16) {{
                    put("name", permissionVO.getName());
                }});
                if (permissions != null && permissions.size() > 0) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("权限名已存在");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
            }

            Permission permission = new Permission();
            BeanUtils.copyProperties(permissionVO, permission);
            permission.setId(UUID.randomUUID().toString());
            permission.setDeletable(1);
            permissionService.save(permission);

            result.setStatus(Result.Status.SUCCESS);
            result.setMessage("success");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
