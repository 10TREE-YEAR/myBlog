package com.wjl.blog.interceptor;

import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.entity.BlogEsContentBean;
import com.wjl.blog.entity.BlogSaveFailBean;
import com.wjl.blog.repository.BlogEsSaveRepository;
import com.wjl.blog.service.EditerService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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

    @Autowired
    private BlogEsSaveRepository blogEsSaveRepository;

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
                // 4.0 存入成功将信息同步es
                BlogEsContentBean blogEsContentBean = this.convertBlogEsContentBean(msg);

                BlogEsContentBean blogContentBean = blogEsSaveRepository.save(blogEsContentBean);
                if(StringUtils.isEmpty(blogContentBean.getId())){
                    BlogSaveFailBean blogSaveFailBean = this.getBlogSaveFailBean("3",msg.getContent());
                    boolean i = editerService.insertBlogSaveFail(blogSaveFailBean);
                }
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


    private BlogEsContentBean convertBlogEsContentBean(BlogContentBean blogContentBean){
        BlogEsContentBean blogEsContentBean = new BlogEsContentBean();

        blogEsContentBean.setContent(blogContentBean.getContent());
        blogEsContentBean.setId(blogContentBean.getId());
        blogEsContentBean.setEndTime(blogContentBean.getEndTime());
        blogEsContentBean.setNumber(blogContentBean.getNumber());
        blogEsContentBean.setSort(blogContentBean.getSort());
        blogEsContentBean.setTitle(blogContentBean.getTitle());
        blogEsContentBean.setStartTime(blogContentBean.getStartTime());
        return blogEsContentBean;
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

}