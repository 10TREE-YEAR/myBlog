package com.wjl.blog.mapper;

import com.wjl.blog.entity.BlogContentBean;
import org.apache.ibatis.annotations.Param;

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

    /** 
    * @Description: 根据ID查询博客内容
    * @Param: [id] 
    * @return: com.wjl.blog.entity.BlogContentBean 
    * @Author: wangjialu
    * @Date: 2020/1/2 
    */ 
    BlogContentBean queryBlogIndex(@Param(value = "id") String id);
}
