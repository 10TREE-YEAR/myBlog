package com.wjl.blog.controller;

import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.entity.ResultInfo;
import com.wjl.blog.service.EditerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.AbstractDocument;
import java.util.ArrayList;
import java.util.List;

/**
 * 博客编辑控制层
 */
@Controller
public class EditController {

    private static final Logger log = LoggerFactory.getLogger(EditController.class);

    @Resource(name = "EditerService")
    private EditerService editerService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

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

        this.rabbitTemplate.convertAndSend("exchange", "topic.messages", resultInfo);
//        // 1.0 存入博客信息
//        if(!StringUtils.isEmpty(blogContentBean.getContent())){
//            boolean i =  editerService.insertBlogContert(blogContentBean);
//            // 2.0 校验是否存入成功
//            if(i){
//                resultInfo.setResultMsg("添加成功！");
//                resultInfo.setResultCode("200");
//            }else{
//                resultInfo.setResultMsg("添加失败！");
//                resultInfo.setResultCode("400");
//            }
//        }else{
//            resultInfo.setResultMsg("请填写内容！！！");
//            resultInfo.setResultCode("400");
//        }
        return resultInfo;
    }

    /**
     * 博客修改页面
     * @return
     */
    @GetMapping(value = "/blogModify.html")
    public ModelAndView getEditModfiyPage(){
        ModelAndView modelAndView = new ModelAndView();
        // 1.0 查询博客信息
        List<BlogContentBean> blogContentBeans = editerService.queryBlogContentList();
        if(blogContentBeans.size()>0){
            modelAndView.addObject("blogContentBeans",blogContentBeans);
        }
        // 2.0 返回页面
        modelAndView.setViewName("/back/pages/blogModify");
        return modelAndView;
    }

    /**
     *  查询需要修改的博客内容，及返回页面
     * @param id
     * @return
     */
    @GetMapping(value = "/editBlog.html")
    public ModelAndView editBlogPage(@RequestParam(value = "id") String id){
        ModelAndView andView = new ModelAndView();
        // 1.0 根据id查询需要修改的博客id内容
        BlogContentBean blogContentBean = editerService.queryBlogContent(id);
        // 2.0 返回页面信息
        andView.addObject("blogContentBean",blogContentBean);
        andView.setViewName("/back/pages/blogEdit");
        return andView;
    }

    /**
     * 修改博客内容
     * @return
     */
    @PostMapping(value = "/getEditBlog.html")
    @ResponseBody
    public ResultInfo editBlogContent(BlogContentBean blogContentBean){
        ResultInfo resultInfo = new ResultInfo();
        try {
            // 1.0 修改博客信息
            boolean i = editerService.updateBlogContent(blogContentBean);
            if(i){
                resultInfo.setResultMsg("修改成功");
                resultInfo.setResultCode("200");
            }else{
                resultInfo.setResultMsg("修改失败");
                resultInfo.setResultCode("400");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setResultMsg("程序报错，请查日志");
            resultInfo.setResultCode("400");
        }
        return resultInfo;
    }

    /**
     * 删除博客信息
     * @param id
     * @return
     */
    @PostMapping(value = "/deleteBlog.html")
    @ResponseBody
    public ResultInfo deleteBlogInfo(@RequestParam(value = "id") String id){
        ResultInfo resultInfo = new ResultInfo();
        // 1.0 根据博客id删除信息（逻辑删除）
        boolean i= editerService.deleteBlogInfo(id);
        // 2.0 返回信息
        if(i){
            resultInfo.setResultMsg("删除成功！");
            resultInfo.setResultCode("200");
        }else{
            resultInfo.setResultMsg("删除失败！");
            resultInfo.setResultCode("400");
        }
        return resultInfo;
    }
}
