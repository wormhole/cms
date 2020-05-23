package net.stackoverflow.cms.web.controller.home;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.constant.UploadPathConst;
import net.stackoverflow.cms.model.entity.Config;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.vo.AuthorityVO;
import net.stackoverflow.cms.security.CmsUserDetails;
import net.stackoverflow.cms.service.ConfigService;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页控制器
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/home")
@Slf4j
public class HomeController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private ConfigService configService;

    /**
     * 获取配置信息
     *
     * @return
     */
    @GetMapping(value = "/config")
    public ResponseEntity config() {
        Result result = new Result();

        List<Config> configs = configService.findAll();
        Map<String, Object> map = new HashMap<>();
        for (Config config : configs) {
            if (config.getKey().equals("head")) {
                if (config.getValue().equals("default"))
                    map.put(config.getKey(), "/head.jpg");
                else
                    map.put(config.getKey(), UploadPathConst.PREFIX + config.getValue());
            } else {
                map.put(config.getKey(), config.getValue());
            }
        }

        result.setMessage("success");
        result.setStatus(Result.Status.SUCCESS);
        result.setData(map);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 获取菜单权限
     *
     * @return
     */
    @GetMapping(value = "/authority")
    public ResponseEntity authority() {
        Result result = new Result();

        CmsUserDetails userDetails = getUserDetails();
        AuthorityVO authorityVO = new AuthorityVO();

        User user = userService.findById(userDetails.getId());
        authorityVO.setUsername(user.getUsername());
        List<Role> roles = userService.findRoleByUserId(user.getId());
        List<Permission> permissions = userService.findPermissionByUserId(user.getId());

        List<String> roleStrs = new ArrayList<>();
        List<String> permissionStrs = new ArrayList<>();

        for (Role role : roles) {
            roleStrs.add(role.getName());
        }
        for (Permission permission : permissions) {
            permissionStrs.add(permission.getName());
        }
        authorityVO.setRoles(roleStrs);
        authorityVO.setPermissions(permissionStrs);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        result.setData(authorityVO);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
