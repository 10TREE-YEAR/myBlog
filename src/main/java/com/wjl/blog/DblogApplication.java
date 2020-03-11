package com.wjl.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@MapperScan(value = "com.wjl.blog.mapper")
//开启扫描搜索引擎的注解
//@EnableElasticsearchRepositories(basePackages = "com.wjl.blog.repository")
@EnableJdbcRepositories(basePackages = "com.wjl.blog.entity")
@EnableCaching
@SpringBootApplication
public class DblogApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(DblogApplication.class, args);
    }

}
