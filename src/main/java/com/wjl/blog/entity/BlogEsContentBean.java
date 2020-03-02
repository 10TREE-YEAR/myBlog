package com.wjl.blog.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 博客内容存入bean
 */
@Data
@Accessors(chain = true)
@Document(type = "java",indexName = "blogContent")
public class BlogEsContentBean implements Serializable {

    @Id
    private String id;// id

    private String title; // 标题
    private String content; // 内容
    private String state; // 状态

//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss" , timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private String startTime;// 开始时间

//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss" , timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private String endTime;// 结束时间
    private String sort;// 排序
    private String number;// 阅读数量

}
