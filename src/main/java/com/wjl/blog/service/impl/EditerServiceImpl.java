package com.wjl.blog.service.impl;

import com.wjl.blog.constant.ElasticSearchConstant;
import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.entity.BlogSaveFailBean;
import com.wjl.blog.entity.BlogTypeBean;
import com.wjl.blog.entity.EsPage;
import com.wjl.blog.mapper.EditerMapper;
import com.wjl.blog.service.EditerService;
import com.wjl.blog.utils.MyElasticSearchUtil;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service(value = "EditerService")
public class EditerServiceImpl implements EditerService {

    private static final Logger log = LoggerFactory.getLogger(EditerServiceImpl.class);

    private static final ThreadLocal<DateFormat> dateTime = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd hh:mm:ss");
        }
    };

    @Resource
    private EditerMapper editerMapper;

    /**
     * 添加博客内容
     * @param blogContentBean
     * @return
     */
    @Override
    public boolean insertBlogContert(BlogContentBean blogContentBean) {
        // 填写博客信息
        String blogId = UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
        blogContentBean.setId(blogId);
        blogContentBean.setStartTime(dateTime.get().format(new Date()));
        blogContentBean.setState("1");
        return editerMapper.insertBlogContert(blogContentBean);
    }

    /**
     * 查询修改的博客信息
     * @return
     */
    @Override
    public List<Map<String, Object>> queryBlogContentList(String type) {
        // 使用es查询博客信息
        EsPage esPage = MyElasticSearchUtil.searchDataPage(ElasticSearchConstant.EsIndexName.BLOG_CONTENT_INDEX_NAME,
                1,10,new TermQueryBuilder("type",type),"","sort",
                "asc","");
        return esPage.getRecordList();
    }

    /**
     * 查询博客单个内容
     * @param id
     * @return
     */
    @Override
    public BlogContentBean queryBlogContent(String id) {
        return editerMapper.queryBlogContent(id);
    }

    /**
     * 修改博客信息
     * @param blogContentBean
     * @return
     */
    @Override
    public boolean updateBlogContent(BlogContentBean blogContentBean) {
        blogContentBean.setEndTime(dateTime.get().format(new Date()));
        int i = editerMapper.updateBlogContent(blogContentBean);
        if(i>0){
            // 修改es博客信息
            try {
                String id = MyElasticSearchUtil.updateData(this.createBuilder(blogContentBean),ElasticSearchConstant.EsIndexName.BLOG_CONTENT_INDEX_NAME,
                        ElasticSearchConstant.EsTypeName.BLOG_CONTENT_TYPE_NAME,blogContentBean.getId());
                if(id.equals(blogContentBean.getId())){
                    return true;
                }else{
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * 删除博客信息
     * @param id
     * @return
     */
    @Override
    public boolean deleteBlogInfo(String id) {
        String time = dateTime.get().format(new Date());
        int i =editerMapper.deleteBlogInfo(id, time);
        if(i>0){
            try {
                MyElasticSearchUtil.deleteData(ElasticSearchConstant.EsIndexName.BLOG_CONTENT_INDEX_NAME,
                        ElasticSearchConstant.EsTypeName.BLOG_CONTENT_TYPE_NAME,id);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }

    /** 
    * @Description: 保存消息队列失败内容
    * @Param: [blogSaveFailBean] 
    * @return: boolean 
    * @Author: wangjialu
    * @Date: 2020/2/17 
    */ 
    @Override
    public boolean insertBlogSaveFail(BlogSaveFailBean blogSaveFailBean) {
        return editerMapper.insertBlogSaveFail(blogSaveFailBean);
    }

    /** 
    * @Description: 查询博客发布类型
    * @Param: [blogWriteType] 
    * @return: java.util.List<com.wjl.blog.entity.BlogTypeBean> 
    * @Author: wangjialu
    * @Date: 2020/3/9 
    */ 
    @Override
    public List<BlogTypeBean> queryBlogTypeList(String blogWriteType) {
        return editerMapper.queryBlogTypeList(blogWriteType);
    }
    private XContentBuilder createBuilder(BlogContentBean blogContentBean){
        XContentBuilder xContentBuilder = null;
        try {
            xContentBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("title",blogContentBean.getTitle())
                    .field("content",blogContentBean.getContent())
                    .field("endTime",blogContentBean.getEndTime())
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xContentBuilder;
    }
}
