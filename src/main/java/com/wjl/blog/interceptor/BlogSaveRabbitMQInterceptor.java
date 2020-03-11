package com.wjl.blog.interceptor;

import com.wjl.blog.constant.ElasticSearchConstant;
import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.entity.BlogEsContentBean;
import com.wjl.blog.entity.BlogSaveFailBean;
import com.wjl.blog.repository.BlogEsSaveRepository;
import com.wjl.blog.service.EditerService;
import com.wjl.blog.utils.ElasticsearchUtil;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.wjl.blog.utils.ConvertBeanUtils.convertBean;

@Component
@RabbitListener(queues = "blog.messages")
public class BlogSaveRabbitMQInterceptor {

    private ThreadLocal<DateFormat> dateTime = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:SS");
        }
    };

    @Resource(name = "EditerService")
    private EditerService editerService;

//    @Autowired
//    private BlogEsSaveRepository blogEsSaveRepository;

    /**
    * @Description: 根据绑定键从队列中获取消息，将消息先存入数据库，在存入es库，如果消息存入数据库失败，则中断存入，
     * 并将存入失败信息保存到消息失败表，进行人工处理。es同步失败将同步失败的消息Id记录，用定时任务重新添加，如果再次添加失败，则进行人工处理。
    * @Param: [msg]
    * @return: void
    * @Author: wangjialu
    * @Date: 2020/3/2
    */
    @RabbitHandler
    public void process(BlogContentBean msg) {
        // 1.0 检测消息内容是否存在
        if(!msg.getContent().isEmpty()){
            // 2.0 保存博客信息
            boolean blog =  editerService.insertBlogContert(msg);
            // 3.0 保存失败需要记下失败内容
            if(!blog){
                BlogSaveFailBean blogSaveFailBean = this.getBlogSaveFailBean("1",msg.getContent());
                boolean i = editerService.insertBlogSaveFail(blogSaveFailBean);
            }
            try {
                // 4.0 检查es中是否存在该索引
                if(!ElasticsearchUtil.isIndexExist(ElasticSearchConstant.EsIndexName.BLOG_CONTENT_INDEX_NAME)){
                    ElasticsearchUtil.createIndex(ElasticSearchConstant.EsIndexName.BLOG_CONTENT_INDEX_NAME,ElasticSearchConstant.EsTypeName.BLOG_CONTENT_TYPE_NAME);
                }
                // 4.1 往索引中添加数据
                ElasticsearchUtil.addData(creatBuolder(msg),ElasticSearchConstant.EsIndexName.BLOG_CONTENT_INDEX_NAME,ElasticSearchConstant.EsTypeName.BLOG_CONTENT_TYPE_NAME,msg.getId());
            } catch (Exception e) {
                e.printStackTrace();
                // 5.0 es运行出错保存信息
               BlogSaveFailBean blogSaveFailBean = this.getBlogSaveFailBean("4",msg.getContent());
               boolean i = editerService.insertBlogSaveFail(blogSaveFailBean);
            }
        }else{
            BlogSaveFailBean blogSaveFailBean = this.getBlogSaveFailBean("2",msg.getContent());
            boolean i = editerService.insertBlogSaveFail(blogSaveFailBean);
        }

    }

    private BlogSaveFailBean getBlogSaveFailBean(String type,String content){
        BlogSaveFailBean blogSaveFailBean = new BlogSaveFailBean();
        blogSaveFailBean.setId(UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
        blogSaveFailBean.setTime(dateTime.get().format(new Date()));
        blogSaveFailBean.setContent(content);
        blogSaveFailBean.setStart("1");
        blogSaveFailBean.setType(type);
        return blogSaveFailBean;
    }

    private XContentBuilder creatBuolder(BlogContentBean blogContentBean){
        XContentBuilder contentBuilder = null;
        try {
            contentBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("id",blogContentBean.getId())
                    .field("content",blogContentBean.getContent())
                    .field("title",blogContentBean.getTitle())
                    .field("startTime",blogContentBean.getStartTime())
                    .field("endTime",blogContentBean.getEndTime())
                    .field("state",blogContentBean.getState())
                    .field("number",blogContentBean.getNumber())
                    .field("type",blogContentBean.getType())
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder;
    }

}