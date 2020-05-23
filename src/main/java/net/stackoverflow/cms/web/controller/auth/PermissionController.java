package net.stackoverflow.cms.web.controller.auth;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.vo.IdsVO;
import net.stackoverflow.cms.model.vo.PermissionVO;
import net.stackoverflow.cms.service.PermissionService;
import net.stackoverflow.cms.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.*;

/**
 * 权限管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/auth/permission")
@Slf4j
@Validated
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
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "key", required = false) String key) {

        Result result = new Result();

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

    }

    /**
     * 删除权限
     *
     * @param idsVO
     * @return
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity delete(@RequestBody @Validated IdsVO idsVO) {
        Result result = new Result();

        //检查是否有不可被删除的
        List<Permission> permissions = permissionService.findByIds(idsVO.getIds());
        for (Permission permission : permissions) {
            if (permission.getDeletable() == 0) {
                throw new BusinessException("包含不允许被删除的权限");
            }
        }

        permissionService.batchDelete(idsVO.getIds());
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 更新权限信息
     *
     * @param permissionVO
     * @return
     */
    @PutMapping(value = "/update")
    public ResponseEntity update(@RequestBody @Validated(PermissionVO.Update.class) PermissionVO permissionVO) {

        Result result = new Result();

        //校验参数
        Permission permission = permissionService.findById(permissionVO.getId());
        if (permission == null) {
            throw new BusinessException("不合法的id");
        }

        if (!permission.getName().equals(permissionVO.getName())) {
            Permission permissions = permissionService.findByName(permission.getName());
            if (permissions != null) {
                throw new BusinessException("权限名已存在");
            }
        }

        permission.setName(permissionVO.getName());
        permission.setDescription(permissionVO.getDescription());
        permissionService.update(permission);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    /**
     * 新增权限
     *
     * @param permissionVO
     * @return
     */
    @PostMapping(value = "/add")
    public ResponseEntity add(@RequestBody @Validated(PermissionVO.Insert.class) PermissionVO permissionVO) {
        Result result = new Result();

        Permission perm = permissionService.findByName(permissionVO.getName());
        if (perm != null) {
            throw new BusinessException("权限名已存在");
        }

        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionVO, permission);
        permission.setId(UUID.randomUUID().toString());
        permission.setDeletable(1);
        permissionService.save(permission);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
