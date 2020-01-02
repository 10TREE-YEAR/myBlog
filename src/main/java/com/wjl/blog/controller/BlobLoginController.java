package com.wjl.blog.controller;

import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.service.BlobLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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


    /** 
    * @Description: 博客首页请求
    * @Param: [] 
    * @return: org.springframework.web.servlet.ModelAndView
    * @Author: wangjialu
    * @Date: 2019/12/30 
    */ 
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

    /** 
    * @Description: 博客内容展示
    * @Param: [] 
    * @return: org.springframework.web.servlet.ModelAndView 
    * @Author: wangjialu
    * @Date: 2020/1/2 
    */ 
    @GetMapping(value = "/single.html")
    public ModelAndView getBlogIndex(@RequestParam(value = "id") String id){
        ModelAndView modelAndView = new ModelAndView();
        // 1.0 检查id是否为空
        if(!StringUtils.isEmpty(id)){
            // 2.0 查询该博客信息，并返回页面
            BlogContentBean blogContentBean = blobLoginService.queryBlogIndex(id);
            modelAndView.setViewName("/blog/single");
        }else{
            modelAndView.addObject("msg","该博客不存在");
            modelAndView.setViewName("/error");
        }
       return modelAndView;
    }
}
