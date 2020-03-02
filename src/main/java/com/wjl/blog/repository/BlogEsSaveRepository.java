package com.wjl.blog.repository;

import com.wjl.blog.entity.BlogEsContentBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
* @Description: 博客保存接口
* @Param:  
* @return:  
* @Author: wangjialu
* @Date: 2020/3/2 
*/ 
public interface BlogEsSaveRepository extends ElasticsearchRepository<BlogEsContentBean,String> {

}
