package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.AuthDTO;
import net.stackoverflow.cms.model.dto.BaseInfoDTO;
import net.stackoverflow.cms.model.dto.PasswordDTO;
import net.stackoverflow.cms.service.BaseInfoService;
import net.stackoverflow.cms.service.MenuService;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 主页相关接口
 *
 * @author 凉衫薄
 */
@RestController
@Slf4j
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private BaseInfoService baseInfoService;

    /**
     * 获取系统基础信息
     *
     * @return
     */
    @GetMapping(value = "/base_info")
    public ResponseEntity<Result<BaseInfoDTO>> baseInfo() {
        BaseInfoDTO dto = baseInfoService.queryBaseInfo();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 修改密码
     *
     * @param dto 密码信息dto对象
     * @return
     */
    @PutMapping(value = "/password")
    public ResponseEntity<Result<Object>> password(@RequestBody @Validated PasswordDTO dto) {
        userService.updatePassword(super.getUserId(), dto.getOld(), dto.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success());
    }

    /**
     * 获取用户信息和菜单权限
     *
     * @return
     */
    @GetMapping(value = "/auth")
    public ResponseEntity<Result<AuthDTO>> auth() {
        List<String> menus = menuService.findKeysByUserId(super.getUserId());
        String username = super.getUsername();
        AuthDTO dto = new AuthDTO(username, menus);
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }
}
