package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.vo.UserAuthorityVO;
import net.stackoverflow.cms.security.CmsUserDetails;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取菜单权限
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/home")
@Slf4j
public class HomeController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 获取菜单权限
     *
     * @return
     */
    @GetMapping(value = "/authority")
    public ResponseEntity authority() {
        Result result = new Result();
        try {
            CmsUserDetails userDetails = getUserDetails();
            UserAuthorityVO userAuthorityVO = new UserAuthorityVO();

            User user = userService.select(userDetails.getId());
            userAuthorityVO.setUsername(user.getUsername());
            List<Role> roles = userService.getRoleByUserId(user.getId());
            List<Permission> permissions = userService.getPermissionByUserId(user.getId());

            List<String> roleStrs = new ArrayList<>();
            List<String> permissionStrs = new ArrayList<>();

            for (Role role : roles) {
                roleStrs.add(role.getName());
            }
            for (Permission permission : permissions) {
                permissionStrs.add(permission.getName());
            }
            userAuthorityVO.setRoles(roleStrs);
            userAuthorityVO.setPermissions(permissionStrs);

            result.setStatus(Result.Status.SUCCESS);
            result.setMessage("success");
            result.setData(userAuthorityVO);
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
