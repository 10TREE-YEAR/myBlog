package com.wjl.blog.config;

import com.wjl.blog.interceptor.LoginHanderInceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/blog").setViewName("blog/index");
    }

    /**
     * 登录拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //  拦截所有请求， 放行 {/ ,/login.html}请求
        registry.addInterceptor(new LoginHanderInceptor()).excludePathPatterns("/","/blog","/toRegister.html","/register.html","/login.html","/static/**");
    }


//    /**
//     * 添加静态资源文件，外部可以直接访问地址
//     * @param registry
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //其他静态资源，与本文关系不大
//        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+ TaleUtils.getUplodFilePath()+"upload/");
//
//        //需要配置1：----------- 需要告知系统，这是要被当成静态文件的！
//        //第一个方法设置访问路径前缀，第二个方法设置资源路径
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//    }
}
