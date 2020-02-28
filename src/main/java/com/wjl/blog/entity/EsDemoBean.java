package com.wjl.blog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* @Description: es测试实体类
* @Param:  
* @return:  
* @Author: wangjialu
* @Date: 2020/2/27 
*/
@Data
@Accessors(chain = true)
@Document(indexName = "demo", type = "java")
public class EsDemoBean implements Serializable {

    @Id
    private String id;

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date time;

}
