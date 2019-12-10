package com.wjl.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 博客编辑控制层
 */
@Controller
public class EditController {

    private static final Logger log = LoggerFactory.getLogger(EditController.class);

    /**
     * 返回编辑页面
     * @return
     */
    @GetMapping(value = "/edit.html")
    public ModelAndView getEditPage(){
        ModelAndView modelAndView = new ModelAndView();
        // 1.0 校验是否有登录信息

        // 2.0 返回页面
        modelAndView.setViewName("/back/pages/edit");
        return modelAndView;
    }
}
