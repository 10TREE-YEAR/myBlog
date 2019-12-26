package com.wjl.blog.mapper;

import com.wjl.blog.entity.BlogContentBean;

import java.util.List;

/**
 * 博客前端页面数据传输接口
 */
public interface BlobLoginMapper {

    /**
     * 查询所有的博客信息
     * @return
     */
    List<BlogContentBean> quertBlogContentList();
}
