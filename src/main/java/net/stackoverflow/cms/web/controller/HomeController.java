package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Config;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.vo.ConfigVO;
import net.stackoverflow.cms.model.vo.UserAuthorityVO;
import net.stackoverflow.cms.security.CmsUserDetails;
import net.stackoverflow.cms.service.ConfigService;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private ConfigService configService;
    @Value("${application.upload.prefix}")
    private String prefix;

    /**
     * 获取配置信息
     *
     * @return
     */
    @GetMapping(value = "/config")
    public ResponseEntity config() {
        Result result = new Result();

        List<Config> configs = configService.findAll();
        List<ConfigVO> configVOs = new ArrayList<>();
        for (Config config : configs) {
            ConfigVO configVO = new ConfigVO();
            BeanUtils.copyProperties(config, configVO);
            if (configVO.getKey().equals("head") && !configVO.getValue().equals("default")) {
                configVO.setValue(prefix + configVO.getValue());
            }
            configVOs.add(configVO);
        }
        result.setMessage("success");
        result.setStatus(Result.Status.SUCCESS);
        result.setData(configVOs);
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
        UserAuthorityVO userAuthorityVO = new UserAuthorityVO();

        User user = userService.findById(userDetails.getId());
        userAuthorityVO.setUsername(user.getUsername());
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
        userAuthorityVO.setRoles(roleStrs);
        userAuthorityVO.setPermissions(permissionStrs);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        result.setData(userAuthorityVO);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
