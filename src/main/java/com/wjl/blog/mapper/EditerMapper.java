package com.wjl.blog.mapper;

import com.wjl.blog.entity.BlogContentBean;
import org.apache.ibatis.annotations.Param;

/**
 * 博客管理数据传输层
 */
public interface EditerMapper {

    /**
     * 添加博客内容
     * @param blogContentBean
     * @return
     */
    boolean insertBlogContert(@Param(value = "blogContentBean") BlogContentBean blogContentBean);
}
