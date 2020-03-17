package com.wjl.blog.controller;

import com.wjl.blog.constant.BlogTypeContant;
import com.wjl.blog.constant.RabbitMQConstant;
import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.entity.BlogMenuBean;
import com.wjl.blog.entity.BlogTypeBean;
import com.wjl.blog.entity.ResultInfo;
import com.wjl.blog.service.EditerService;
import com.wjl.blog.service.LoginService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 博客编辑控制层
 */
@Api(tags = "博客编辑控制层")
@Controller
public class EditController {

    private static final Logger log = LoggerFactory.getLogger(EditController.class);

    @Resource(name = "EditerService")
    private EditerService editerService;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Resource(name = "loginServiceImpl")
    private LoginService loginService;

    /**
     * 返回编辑页面
     * @return
     */
    @ApiOperation(value = "返回编辑页面",notes = "需要查询菜单信息")
    @GetMapping(value = "/blogEdit.html")
    public ModelAndView getEditPage(){
        ModelAndView modelAndView = new ModelAndView();
        // 1.0 查询菜单信息
        List<BlogMenuBean> blogMenuBeans = loginService.queryBlogMenuBeans();
        modelAndView.addObject("blogMenuBeans",blogMenuBeans);
        // 1.1 查询博客类型信息
        List<BlogTypeBean> blogTypeBeans = editerService.queryBlogTypeList(BlogTypeContant.BLOG_WRITE_TYPE);
        // 2.0 返回页面
        modelAndView.addObject("blogTypeBeans",blogTypeBeans);
        modelAndView.setViewName("/back/pages/edit");
        return modelAndView;
    }

    /**
     * 获取返回信息
     * @param blogContentBean
     * @return
     */
    @ApiOperation(value = "获取返回信息")
    @ApiImplicitParam(name = "blogContentBean",value = "博客内容返回信息",required = true,dataType = "com.wjl.blog.entity.BlogContentBean")
    @ApiResponses({@ApiResponse(code = 200,message = "添加成功"), @ApiResponse(code = 400,message = "没有博客内容")})
    @PostMapping(value = "/addBlog.html")
    @ResponseBody
    public ResultInfo addBlogContent(BlogContentBean blogContentBean){
        ResultInfo resultInfo = new ResultInfo();
        log.info(blogContentBean.getContent());// 打印博客内容
        // 1.0 生产者传入消息
        this.rabbitTemplate.convertAndSend(RabbitMQConstant.BLOG_EXCHANGE, RabbitMQConstant.RabbitMQRoutKey.BLOG_SAVE_ROUT_KEY, blogContentBean);
        // 2.0 返回博客信息
        if(!StringUtils.isEmpty(blogContentBean.getContent())){
            resultInfo.setResultMsg("添加成功！");
            resultInfo.setResultCode("200");
        }else{
            resultInfo.setResultMsg("请填写内容！！！");
            resultInfo.setResultCode("400");
        }
        return resultInfo;
    }

    /**
     * 博客修改页面
     * @return
     */
    @GetMapping(value = "/blogModify.html")
    public ModelAndView getEditModfiyPage(@RequestParam(value = "type") String type){
        ModelAndView modelAndView = new ModelAndView();
        // 1.0 查询菜单信息
        List<BlogMenuBean> blogMenuBeans = loginService.queryBlogMenuBeans();
        modelAndView.addObject("blogMenuBeans",blogMenuBeans);
        // 2.0 查询博客信息
        List<Map<String, Object>> blogContentBeans = editerService.queryBlogContentList(type);
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
    public ModelAndView editBlogPage(@RequestParam(value = "id") String id, @RequestParam(value = "type") String type){
        ModelAndView andView = new ModelAndView();
        // 1.0 查询菜单信息
        List<BlogMenuBean> blogMenuBeans = loginService.queryBlogMenuBeans();
        andView.addObject("blogMenuBeans",blogMenuBeans);
        // 2.0 根据id查询需要修改的博客id内容
        BlogContentBean blogContentBean = editerService.queryBlogContent(id);
        // 2.0 返回页面信息
        andView.addObject("blogContentBean",blogContentBean);
        andView.addObject("blogType",type);
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
            this.rabbitTemplate.convertAndSend(RabbitMQConstant.BLOG_EXCHANGE, RabbitMQConstant.RabbitMQRoutKey.BLOG_EDIT_ROUT_KEY, blogContentBean);
            //boolean i = editerService.updateBlogContent(blogContentBean);
            if(!StringUtils.isEmpty(blogContentBean.getContent())){
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
