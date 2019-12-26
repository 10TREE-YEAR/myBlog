package com.wjl.blog.controller;

import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.service.BlobLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;


/**
 * 博客首页展示
 */
@Controller
public class BlobLoginController {

    private static final Logger log = LoggerFactory.getLogger(BlobLoginController.class);

    @Resource(name = "BlobLoginServiceImpl")
    private BlobLoginService blobLoginService;

    @GetMapping(value = {"/blogIndex.html","/blog.html"})
    public ModelAndView getBlobIndexPage(){
        ModelAndView modelAndView = new ModelAndView();
        // 1.0 查询所有的博客信息
        List<BlogContentBean> blogContentBeans = blobLoginService.quertBlogContentList();
        // 2.0 返回信息
        modelAndView.addObject("blogContentBeans",blogContentBeans);
        modelAndView.setViewName("/blog/index");
        return  modelAndView;
    }

}
