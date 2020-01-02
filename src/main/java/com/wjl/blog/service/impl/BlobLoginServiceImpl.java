package com.wjl.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wjl.blog.constant.BlogRedisKeyConstant;
import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.mapper.BlobLoginMapper;
import com.wjl.blog.service.BlobLoginService;
import com.wjl.blog.utils.RedisHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 博客前端业务处理控制器
 */
@Service(value = "BlobLoginServiceImpl")
public class BlobLoginServiceImpl implements BlobLoginService {

    @Resource
    private BlobLoginMapper blobLoginMapper;

    @Resource(name = "redisHelper")
    private RedisHelper redisHelper;

    /**
     * 查询所有的博客信息
     * @return
     */
    @Override
    public List<BlogContentBean> quertBlogContentList() {
        // 1.0 查询缓存是否存博客信息
        String i = (String) redisHelper.getValue(BlogRedisKeyConstant.BLOG_INDEX_KEY);
        // 2.0 没有从数据库中查询
        if(null != i){
            List<BlogContentBean> blogContentBeans = JSONObject.parseArray(i,BlogContentBean.class);
            return blogContentBeans;
        }else{
            List<BlogContentBean> blogContentBean = blobLoginMapper.quertBlogContentList();
            redisHelper.valuePut(BlogRedisKeyConstant.BLOG_INDEX_KEY, JSON.toJSON(blogContentBean).toString());
            return blogContentBean;
        }
    }

    /** 
    * @Description: 根据ID查询博客内容
     * @Param: [id] 
    * @return: com.wjl.blog.entity.BlogContentBean 
    * @Author: wangjialu
    * @Date: 2020/1/2 
    */ 
    @Override
    public BlogContentBean queryBlogIndex(String id) {
        // 1.0 查询缓存是否存博客信息
        String i = (String) redisHelper.getValue(BlogRedisKeyConstant.BLOG_ONE_INDEX_KEY+id);
        // 2.0 没有从数据库中查询
       if(null != i){
           BlogContentBean blogContentBean = JSONArray.parseObject(i,BlogContentBean.class);
           return blogContentBean;
       }else{
           BlogContentBean blogContentBean = blobLoginMapper.queryBlogIndex(id);
           redisHelper.valuePut(BlogRedisKeyConstant.BLOG_ONE_INDEX_KEY+id, JSONArray.toJSON(blogContentBean).toString());
           return blogContentBean;
       }
    }
}
