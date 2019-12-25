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
}
