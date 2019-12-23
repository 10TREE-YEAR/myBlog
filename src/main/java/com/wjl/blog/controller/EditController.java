package com.wjl.blog.controller;

import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.entity.ResultInfo;
import com.wjl.blog.service.EditerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.AbstractDocument;

/**
 * 博客编辑控制层
 */
@Controller
public class EditController {

    private static final Logger log = LoggerFactory.getLogger(EditController.class);

    @Resource(name = "EditerService")
    private EditerService editerService;

    /**
     * 返回编辑页面
     * @return
     */
    @GetMapping(value = "/blogEdit.html")
    public ModelAndView getEditPage(){
        ModelAndView modelAndView = new ModelAndView();
        // 1.0 校验是否有登录信息

        // 2.0 返回页面
        modelAndView.setViewName("/back/pages/edit");
        return modelAndView;
    }

    /**
     * 获取返回信息
     * @param blogContentBean
     * @return
     */
    @PostMapping(value = "/addBlog.html")
    @ResponseBody
    public ResultInfo addTest(BlogContentBean blogContentBean){
        ResultInfo resultInfo = new ResultInfo();
        log.info(blogContentBean.getContent());// 打印博客内容
        // 1.0 存入博客信息
        if(!StringUtils.isEmpty(blogContentBean.getContent())){
            boolean i =  editerService.insertBlogContert(blogContentBean);
            // 2.0 校验是否存入成功
            if(i){
                resultInfo.setResultMsg("添加成功！");
                resultInfo.setResultCode("200");
            }else{
                resultInfo.setResultMsg("添加失败！");
                resultInfo.setResultCode("400");
            }
        }else{
            resultInfo.setResultMsg("请填写内容！！！");
            resultInfo.setResultCode("400");
        }
        return resultInfo;
    }

    /**
     * 返回博客修改页面
     * @return
     */
    @GetMapping(value = "/blogModify.html")
    public ModelAndView getEditModfiyPage(){
        ModelAndView modelAndView = new ModelAndView();
        // 1.0 校验是否有登录信息

        // 2.0 返回页面
        modelAndView.setViewName("/back/pages/blogModify");
        return modelAndView;
    }

}
