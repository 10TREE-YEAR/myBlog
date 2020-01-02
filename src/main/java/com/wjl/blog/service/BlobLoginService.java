package com.wjl.blog.service;

import com.wjl.blog.entity.BlogContentBean;

import java.util.List;

/**
 * 博客前端页面业务处理接口
 */
public interface BlobLoginService {

    /**
     * 查询所有的博客信息
     * @return
     */
    List<BlogContentBean> quertBlogContentList();

    /** 
    * @Description: 根据ID查询博客内容
    * @Param: [id] 
    * @return: com.wjl.blog.entity.BlogContentBean 
    * @Author: wangjialu
    * @Date: 2020/1/2 
    */ 
    BlogContentBean queryBlogIndex(String id);
}
