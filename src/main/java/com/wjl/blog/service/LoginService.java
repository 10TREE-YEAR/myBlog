package com.wjl.blog.service;

import com.wjl.blog.entity.BlogMenuBean;
import com.wjl.blog.entity.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LoginService {

    boolean toUserInfo(UserInfo userInfo);

    /**
     * 查询是否存在用户信息
     * @param userNum
     * @return
     */
    Boolean queryExistUserNum(String userNum);

    /**
     * 存入用户信息
     * @param userInfo
     * @return
     */
    Boolean intsertUser(UserInfo userInfo);

    /**
     * 查询是否存在用户信息
     * @param loginUsername
     * @param convertMD5
     * @return
     */
    boolean queryUserInfo(String loginUsername, String convertMD5);

    /**
     * 查询所有的一级菜单
     * @return
     */
    List<BlogMenuBean> queryBlogMenuBeans();
}
