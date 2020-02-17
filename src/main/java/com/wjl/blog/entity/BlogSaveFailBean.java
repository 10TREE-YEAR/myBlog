package com.wjl.blog.entity;

import lombok.Data;

@Data
public class BlogSaveFailBean {

    private String id;// 失败消息的id
    private String content;// 失败消息的内容
    private String start;// 失败消息的状态
    private String time;// 失败消息的时间
    private String blogPeople;// 失败消息的内容创建人
    private String type;// 类型：1 添加失败 2 消息内容不存在失败
}
