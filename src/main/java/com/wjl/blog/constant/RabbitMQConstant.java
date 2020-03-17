package com.wjl.blog.constant;

/**
 * @Description: 消息队列常量类
 * @param @param
 * @return @return
 * @author wangjialu
 * @throws
 * @date 2020/3/16 20:13
 */
public class RabbitMQConstant {

    public static final String BLOG_EXCHANGE = "blog_exchange"; // 消息交换器

    public static final String BLOG_QUEUE = "blog.save.queue";// 消息新增队列
    public static final String BLOG_EDIT_QUEUE = "blog.edit.queue";// 消息修改队列

    /**
     * @Description: 消息队列路由
     * @param @param
     * @return @return
     * @author wangjialu
     * @throws
     * @date 2020/3/17 19:09
     */
    public static final class RabbitMQRoutKey{
        public static final String BLOG_SAVE_ROUT_KEY = "blog.messages.save";// 消息保存路由键
        public static final String BLOG_EDIT_ROUT_KEY = "blog.messages.edit";// 消息修改路由键
    }
}
