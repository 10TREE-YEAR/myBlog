package com.wjl.blog.interceptor;

import com.wjl.blog.entity.BlogContentBean;
import com.wjl.blog.entity.BlogSaveFailBean;
import com.wjl.blog.service.EditerService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
@RabbitListener(queues = "blog.messages")
public class BlogSaveRabbitMQInterceptor {

    private ThreadLocal<DateFormat> dateTime = new ThreadLocal<DateFormat>(){
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd hhmmSS");
        }
    };

    @Resource(name = "EditerService")
    private EditerService editerService;

    @RabbitHandler
    public void process(BlogContentBean msg) {
        // 1.0 检测消息内容是否存在
        if(!msg.getContent().isEmpty()){
            // 2.0 保存博客信息
            boolean blog =  editerService.insertBlogContert(msg);
            // 3.0 保存失败需要记下失败内容
            if(!blog){
                BlogSaveFailBean blogSaveFailBean = new BlogSaveFailBean();
                blogSaveFailBean.setId(UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
                blogSaveFailBean.setTime(dateTime.get().format(new Date()));
                blogSaveFailBean.setContent(msg.getContent());
                blogSaveFailBean.setStart("1");
                blogSaveFailBean.setType("1");
                boolean i = editerService.insertBlogSaveFail(blogSaveFailBean);
            }
        }else{
            BlogSaveFailBean blogSaveFailBean = new BlogSaveFailBean();
            blogSaveFailBean.setId(UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
            blogSaveFailBean.setTime(dateTime.get().format(new Date()));
            blogSaveFailBean.setContent(msg.getContent());
            blogSaveFailBean.setStart("1");
            blogSaveFailBean.setType("2");
            boolean i = editerService.insertBlogSaveFail(blogSaveFailBean);
        }

    }

}
