package com.wjl.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 欢迎页控制层
 */
@Controller
public class WecolmeController {

    private static final Logger log = LoggerFactory.getLogger(WecolmeController.class);

    @GetMapping(value = "/welcome.html")
    public ModelAndView getWelocomePath(){
        ModelAndView andView = new ModelAndView();
        //TODO 目前先没有业务逻辑，以后添加
        // 1.0 返回页面
        andView.setViewName("/back/pages/index");
        return andView;
    }

}
