package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.model.vo.RegisterVO;
import net.stackoverflow.cms.security.CmsMd5PasswordEncoder;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.JsonUtils;
import net.stackoverflow.cms.util.ValidateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 用户注册Controller
 *
 * @author 凉衫薄
 */
@RestController
@Slf4j
public class RegisterController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 用户注册接口
     *
     * @param registerVO
     * @param session
     * @return
     */
    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody RegisterVO registerVO, HttpSession session) {

        log.info("请求参数：" + JsonUtils.bean2json(registerVO));

        Result result = new Result();
        try {
            //校验验证码
            String vcode = (String) session.getAttribute("vcode");
            if (!vcode.equalsIgnoreCase(registerVO.getVcode())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("验证码错误");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            //校验数据
            if (!ValidateUtils.validateUsername(registerVO.getUsername())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("用户名不能为空");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                List<User> users = userService.selectByCondition(new HashMap<String, Object>(16) {{
                    put("username", registerVO.getUsername());
                }});
                if (users.size() != 0) {
                    result.setStatus(Result.Status.FAILURE);
                    result.setMessage("用户名重复");
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                }
            }
            if (!ValidateUtils.validateTelephone(registerVO.getTelephone())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("电话号码格式错误");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            if (!ValidateUtils.validateEmail(registerVO.getEmail())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("邮箱格式错误");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            if (!ValidateUtils.validatePassword(registerVO.getPassword())) {
                result.setStatus(Result.Status.FAILURE);
                result.setMessage("密码长度不能小于6");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            //保存至数据库
            User user = new User();
            BeanUtils.copyProperties(registerVO, user);
            user.setId(UUID.randomUUID().toString());
            user.setEnabled(1);
            user.setDeletable(1);
            user.setPassword(new CmsMd5PasswordEncoder().encode(user.getPassword()));
            userService.insert(user);

            //默认授予访客角色及权限
            List<Role> roles = roleService.selectByCondition(new HashMap<String, Object>(16) {{
                put("name", "guest");
            }});
            if (roles != null && roles.size() == 1) {
                Role guest = roles.get(0);
                userService.grantRole(user.getId(), guest.getId());
            }

            result.setStatus(Result.Status.SUCCESS);
            result.setMessage("注册成功");
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
