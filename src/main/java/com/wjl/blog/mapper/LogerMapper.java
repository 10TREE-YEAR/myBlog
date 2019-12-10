package com.wjl.blog.mapper;

import com.wjl.blog.entity.BlogMenuBean;
import com.wjl.blog.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息传输层
 */
public interface LogerMapper {

    /**
     * 查询是否存在用户信息
     * @param userNum
     * @return
     */
    String queryExistUserNum(@Param(value = "userNum") String userNum);

    /**
     * 存入用户信息
     * @param userInfo
     * @return
     */
    int intsertUser(@Param(value = "userInfo") UserInfo userInfo);

    /**
     * 查询是否存在用户信息
     * @param loginUsername
     * @param password
     * @return
     */
    int queryUserInfo(@Param("loginUsername") String loginUsername, @Param("password") String password);

    /**
     * 查询所有的一级菜单
     * @return
     */
    List<BlogMenuBean> queryBlogMenuBeans();
}
