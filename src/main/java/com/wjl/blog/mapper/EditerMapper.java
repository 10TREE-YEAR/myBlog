package com.wjl.blog.mapper;

import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.entity.BlogSaveFailBean;
import com.wjl.blog.entity.BlogTypeBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 查询博客信息
     * @return
     */
    List<BlogContentBean> queryBlogContentList(@Param(value = "type") String type);

    /**
     * 查询博客单个内容
     * @param id
     * @return
     */
    BlogContentBean queryBlogContent(@Param(value = "id") String id);

    /**
     *  修改博客信息
     * @param blogContentBean
     * @return
     */
    int updateBlogContent(@Param(value = "blogContentBean") BlogContentBean blogContentBean);

    /**
     * 删除博客信息
     * @param id
     * @param time
     * @return
     */
    int deleteBlogInfo(@Param(value = "id") String id, @Param(value = "time") String time);

    /** 
    * @Description: 保存消息失败数据传输接口
    * @Param: [blogSaveFailBean] 
    * @return: boolean 
    * @Author: wangjialu
    * @Date: 2020/2/17 
    */ 
    boolean insertBlogSaveFail(@Param(value = "blogSaveFailBean") BlogSaveFailBean blogSaveFailBean);

    /** 
    * @Description: 查询博客发布接口
    * @Param: [blogWriteType] 
    * @return: java.util.List<com.wjl.blog.entity.BlogTypeBean> 
    * @Author: wangjialu
    * @Date: 2020/3/9 
    */ 
    List<BlogTypeBean> queryBlogTypeList(@Param(value = "blogWriteType") String blogWriteType);
}
