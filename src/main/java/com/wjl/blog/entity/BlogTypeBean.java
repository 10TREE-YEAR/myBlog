package com.wjl.blog.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

/**
* @Description: 系统类型实体类
* @Param:
* @return:
* @Author: wangjialu
* @Date: 2020/3/9
*/
@Data
@Slf4j
public class BlogTypeBean {

    @Id
    private String id; // 类型ID
    private String name; // 类型名称
    private String sort; // 类型排序
    private String start; // 类型状态
    private String type; // 博客识别类型
    private String code; // 类型编码 （1：博客类型）
}
