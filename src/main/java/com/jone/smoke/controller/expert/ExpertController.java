package com.jone.smoke.controller.expert;

import com.jone.smoke.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@EnableAutoConfiguration
@RequestMapping(value="expert")
public class ExpertController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ExpertController.class);

    @RequestMapping(value = "/list")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("expert/list");
        //mv.addObject("title", systemProperties.getName());
        return mv;
    }
}
