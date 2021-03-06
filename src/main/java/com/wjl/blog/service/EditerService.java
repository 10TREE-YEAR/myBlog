package com.wjl.blog.service;


import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.entity.BlogSaveFailBean;
import com.wjl.blog.entity.BlogTypeBean;
import com.wjl.blog.entity.EsPage;

import java.util.List;
import java.util.Map;

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
    EsPage queryBlogContentList(int currentPage, int pageSize, String type);

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


    /**
    * @Description:  添加博客创建失败内容
    * @Param: [blogSaveFailBean]
    * @return: boolean
    * @Author: wangjialu
    * @Date: 2020/2/17
    */
    boolean insertBlogSaveFail(BlogSaveFailBean blogSaveFailBean);

    /** 
    * @Description: 查询博客发布类型
    * @Param: [blogWriteType] 
    * @return: java.util.List<com.wjl.blog.entity.BlogTypeBean> 
    * @Author: wangjialu
    * @Date: 2020/3/9 
    */ 
    List<BlogTypeBean> queryBlogTypeList(String blogWriteType);
}
