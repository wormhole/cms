package net.stackoverflow.cms.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @RequestMapping(value = {"/admin", "/admin/index"}, method = RequestMethod.GET)
    public ModelAndView admin() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/admin/index");
        return mv;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/admin");
        return mv;
    }
}
