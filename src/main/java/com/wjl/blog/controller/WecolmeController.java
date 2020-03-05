package com.wjl.blog.controller;

import com.wjl.blog.entity.BlogMenuBean;
import com.wjl.blog.mapper.LogerMapper;
import com.wjl.blog.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 欢迎页控制层
 */
@Controller
public class WecolmeController {

    private static final Logger log = LoggerFactory.getLogger(WecolmeController.class);

    @Resource(name = "loginServiceImpl")
    private LoginService loginService;

    @GetMapping(value = "/welcome.html")
    public ModelAndView getWelocomePath(){
        ModelAndView andView = new ModelAndView();
        //TODO 目前先没有业务逻辑，以后添加
        // 1.0 返回页面
        // 2.0 查询菜单信息
        List<BlogMenuBean> blogMenuBeans = loginService.queryBlogMenuBeans();

        andView.addObject("blogMenuBeans",blogMenuBeans);
        andView.setViewName("/back/pages/index");
        return andView;
    }

}
