package com.wjl.blog.service;


import com.wjl.blog.entity.BlogContentBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *博客业务处理接口
 */
public interface EditerService {

    /**
     * 添加博客内容
     * @param blogContentBean
     * @return
     */
    boolean insertBlogContert(BlogContentBean blogContentBean);

    /**
     * 查询博客内容信息
     * @return
     */
    List<BlogContentBean> queryBlogContentList();

    /**
     * 查询单个博客内容
     * @param id
     * @return
     */
    BlogContentBean queryBlogContent(String id);

    /**
     * 修改博客信息
     * @param blogContentBean
     * @return
     */
    boolean updateBlogContent(BlogContentBean blogContentBean);

    /**
     * 删除博客信息
     * @param id
     * @return
     */
    boolean deleteBlogInfo(String id);
}
