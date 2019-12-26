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
}
