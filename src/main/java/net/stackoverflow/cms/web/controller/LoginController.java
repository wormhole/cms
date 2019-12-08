package net.stackoverflow.cms.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录接口
 *
 * @author 凉衫薄
 */
@Controller
public class LoginController {

    /**
     * 登录页面跳转
     *
     * @return 返回ModelAndView
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/login");
        return mv;
    }
}
