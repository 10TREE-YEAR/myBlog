package com.wjl.blog.repository;

import com.wjl.blog.entity.EsDemoBean;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

// 整合es存在问题 ElasticSearch的bean会和JDBC的bean存在冲突
/**
* @Description: es demo接口
* @Param:  
* @return:  
* @Author: wangjialu
* @Date: 2020/2/27 
*/
public interface EsDemoRepository extends ElasticsearchRepository<EsDemoBean,String> {
}

