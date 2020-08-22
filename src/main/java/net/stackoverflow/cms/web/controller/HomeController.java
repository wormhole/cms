package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.constant.UploadPathConst;
import net.stackoverflow.cms.model.dto.AuthorityDTO;
import net.stackoverflow.cms.model.dto.PropertyDTO;
import net.stackoverflow.cms.security.CmsUserDetails;
import net.stackoverflow.cms.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 主页控制器
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/home")
@Slf4j
public class HomeController extends BaseController {

    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private PropertyService propertyService;

    /**
     * 获取配置信息
     *
     * @return
     */
    @GetMapping(value = "/properties")
    public ResponseEntity<Result<Map<String, String>>> properties() {

        List<PropertyDTO> propertyDTOS = propertyService.findByKeys(Arrays.asList("title", "head", "copyright", "rememberMe"));
        Map<String, String> map = new HashMap<>(16);
        for (PropertyDTO propertyDTO : propertyDTOS) {
            if ("head".equals(propertyDTO.getKey())) {
                if ("default".equals(propertyDTO.getValue())) {
                    map.put(propertyDTO.getKey(), "/head.jpg");
                } else {
                    map.put(propertyDTO.getKey(), UploadPathConst.PREFIX + propertyDTO.getValue());
                }
            } else {
                map.put(propertyDTO.getKey(), propertyDTO.getValue());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", map));
    }

    /**
     * 获取菜单权限
     *
     * @return
     */
    @GetMapping(value = "/authority")
    public ResponseEntity<Result<AuthorityDTO>> authority() {

        CmsUserDetails userDetails = (CmsUserDetails) super.getUserDetails();
        AuthorityDTO authorityDTO = new AuthorityDTO();

        List<SimpleGrantedAuthority> sgas = (List<SimpleGrantedAuthority>) userDetails.getAuthorities();

        List<String> roles = new ArrayList<>();
        List<String> permissions = new ArrayList<>();

        sgas.forEach(sga -> {
            String authority = sga.getAuthority();
            if (authority.startsWith(ROLE_PREFIX)) {
                roles.add(authority.substring(5));
            } else {
                permissions.add(authority);
            }
        });
        authorityDTO.setUsername(userDetails.getUsername());
        authorityDTO.setRoles(roles);
        authorityDTO.setPermissions(permissions);

        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", authorityDTO));

    }
}
