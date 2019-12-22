package net.stackoverflow.cms.web.controller.user;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.vo.IdsVO;
import net.stackoverflow.cms.model.vo.RoleVO;
import net.stackoverflow.cms.model.vo.UserVO;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/user/user_manage")
@Slf4j
public class UserManageController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

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
            //校验参数
            if (page < 1) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("page不能小于1");
                log.error("page小于1:" + page);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            if (limit < 1) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("limit不能小于1");
                log.error("limit小于1:" + limit);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

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
            List<UserVO> userVOs = new ArrayList<>();
            Map<String, Object> searchMap = new HashMap<>(16);
            int total = 0;

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
            List<User> users = userService.selectByPage(pageParam);
            pageParam.setLimit(null);
            pageParam.setOffset(null);
            total = userService.selectByPage(pageParam).size();

            for (User user : users) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                List<Role> roles = userService.getRoleByUserId(user.getId());
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
            e.printStackTrace();
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("服务器错误");
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody IdsVO idsVO) {
        Result result = new Result();
        try {
            //校验参数
            if (idsVO.getIds() == null || idsVO.getIds().size() == 0) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("参数不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            //检查是否有不可被删除的
            Map<String, Object> searchMap = new HashMap<>(16);
            searchMap.put("ids", idsVO.getIds());
            List<User> users = userService.selectByCondition(searchMap);
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
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch (Exception e) {
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("服务器错误");
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
