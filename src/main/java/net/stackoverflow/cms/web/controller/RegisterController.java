package net.stackoverflow.cms.web.controller;

import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.pojo.entity.User;
import net.stackoverflow.cms.pojo.vo.UserVO;
import net.stackoverflow.cms.security.CmsMd5PasswordEncoder;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 用户注册Controller
 *
 * @author 凉衫薄
 */
@Controller
public class RegisterController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param userVO
     * @param session
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity register(@RequestBody UserVO userVO, HttpSession session) {

        Result result = new Result();
        try {
            //校验验证码
            String vcode = (String) session.getAttribute("vcode");
            if (!vcode.equalsIgnoreCase(userVO.getVcode())) {
                Map<String, String> errorMap = new HashMap<>(16);
                errorMap.put("vcode", "验证码错误");
                result.setStatus(Result.Status.FAILURE);
                result.setData(errorMap);
                result.setMessage("验证码错误");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }

            //保存至数据库
            User user = new User();
            BeanUtils.copyProperties(userVO, user);
            user.setId(UUID.randomUUID().toString());
            user.setEnabled(1);
            user.setPassword(new CmsMd5PasswordEncoder().encode(user.getPassword()));
            userService.insert(user);

            result.setStatus(Result.Status.SUCCESS);
            result.setMessage("注册成功");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("服务器错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
