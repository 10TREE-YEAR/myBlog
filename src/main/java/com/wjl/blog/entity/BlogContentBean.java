package com.wjl.blog.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 博客内容存入bean
 */
@Data
public class BlogContentBean implements Serializable {
    private String id;// id
    private String title; // 标题
    private String content; // 内容
    private String state; // 状态
    private String startTime;// 开始时间
    private String endTime;// 结束时间
    private String sort;// 排序
    private String number;// 阅读数量

}
