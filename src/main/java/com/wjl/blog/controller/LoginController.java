package com.wjl.blog.controller;

import com.wjl.blog.constant.UserLoginConstant;
import com.wjl.blog.entity.BlogMenuBean;
import com.wjl.blog.entity.UserInfo;
import com.wjl.blog.service.LoginService;
import com.wjl.blog.utils.MD5Util;
import com.wjl.blog.utils.RedisHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户登录控制层
 */
@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private static final ThreadLocal<DateFormat> dateTime = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("YYYY-MM-dd HH:MM:SS");
        }
    };

    private static final String USER_USER_STATE = "1";// 用户使用状态

    @Resource(name = "loginServiceImpl")
    private LoginService loginService;

    @Resource(name = "redisHelper")
    private RedisHelper redisHelper;

    /**
     * 用户登录处理
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/login.html")
    public String toLogin(@RequestParam(value = "loginUsername") String loginUsername,
                                @RequestParam(value = "loginPassword") String  loginPassword,
                                HttpServletRequest request, HttpServletResponse response, HttpSession session){

        // 1.0 查询是否存在该用户信息
        try {
            boolean userInfo = loginService.queryUserInfo(loginUsername,MD5Util.convertMD5(loginPassword));
            if(userInfo){
                // 1.1 查看session是否存在用户信息
                if(null == session.getAttribute("username")){
                    // 1.2 将用信息放入session中
                    session.setAttribute("username",loginUsername);
                }
                // 1.3 查看redis中是否存在用户信息
                if(null == redisHelper.getValue(loginUsername)){
                    redisHelper.valuePut(loginUsername,dateTime.get().format(new Date()));
                }
                return "redirect:/welcome.html";
            }else{
                // 2.0 传入用户信息进行业务处理
                return "redirect:/index.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/index.html";
    }

    /**
     * 跳转用户注册页面
     */
    @GetMapping(value = "/toRegister.html")
    public ModelAndView toRegister(HttpServletRequest request,HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/register");
        return modelAndView;
    }

    /**
     * 注册用户
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/register.html")
    public ModelAndView registerUser(HttpServletRequest request, HttpServletResponse response, UserInfo userInfo){
        ModelAndView modelAndView = new ModelAndView();

        try {
            // 1.0校验账号是否重复
            Boolean existUserNum = loginService.queryExistUserNum(userInfo.getUserNum());
            if(existUserNum){
                userInfo.setUserID(UUID.randomUUID().toString().replaceAll("- ","").toUpperCase());
                userInfo.setUserPassword(MD5Util.convertMD5(userInfo.getUserPassword()));
                userInfo.setUserStartTime(this.dateTime.get().format(new Date()));
                userInfo.setUserStart(this.USER_USER_STATE);
                // 2.0存入账号信息
                Boolean addUser = loginService.intsertUser(userInfo);
                if(addUser){
                    modelAndView.setViewName("/login");
                }else{
                    modelAndView.setViewName("/error");
                    modelAndView.addObject("msg","注册失败，请重新注册！！！");
                }
            }else{
                modelAndView.addObject("msg","该用户名已存在，请重新填写！！！");
                modelAndView.setViewName("/error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("/error");
        }
        return modelAndView;
    }
}
