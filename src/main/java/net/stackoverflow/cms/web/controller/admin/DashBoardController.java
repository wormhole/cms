package net.stackoverflow.cms.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashBoardController {

    @RequestMapping(value = "/admin/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/dashboard");
        return mv;
    }
}
