package com.wjl.blog.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfig {


    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
        return new DruidDataSource();
    }

    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");
        Map<String, String> map = new HashMap<String, String>();
        map.put("loginUsername","admin");
        map.put("loginPassword","123456");
        map.put("allow","");//允许所有访问
        servletRegistrationBean.setInitParameters(map);
        return  servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        filterRegistrationBean.setFilter(new WebStatFilter());
        Map<String, String> map = new HashMap<String, String>();
        map.put("exclusions","*.js,*.css,/druid/*");

        filterRegistrationBean.setInitParameters(map);
        return  filterRegistrationBean;
    }

}
