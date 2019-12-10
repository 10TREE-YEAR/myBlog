package com.wjl.blog.service.impl;

import com.wjl.blog.entity.BlogMenuBean;
import com.wjl.blog.entity.UserInfo;
import com.wjl.blog.mapper.LogerMapper;
import com.wjl.blog.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "loginServiceImpl")
public class LoginServiceImpl implements LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private LogerMapper logerMapper;

    /**
     * 进行处理用户信息
     * @param userInfo
     * @return
     */
    @Override
    public boolean toUserInfo(UserInfo userInfo) {
        return false;
    }

    /**
     * 查询是否存在用户信息
     * @param userNum
     * @return
     */
    @Override
    public Boolean queryExistUserNum(String userNum) {
        String i = logerMapper.queryExistUserNum(userNum);
        if(i != null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 存入用户信息
     * @param userInfo
     * @return
     */
    @Override
    public Boolean intsertUser(UserInfo userInfo) {
        int i = logerMapper.intsertUser(userInfo);
        if(i  > 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 查询是否存在用户信息
     * @param loginUsername
     * @param convertMD5
     * @return
     */
    @Override
    public boolean queryUserInfo(String loginUsername, String password) {
        int i = logerMapper.queryUserInfo(loginUsername, password);
        if(i>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 查询所有的一级菜单
     * @return
     */
    @Override
    public List<BlogMenuBean> queryBlogMenuBeans() {
        return logerMapper.queryBlogMenuBeans();
    }
}
