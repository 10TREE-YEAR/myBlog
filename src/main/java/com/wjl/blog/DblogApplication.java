package com.wjl.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan(value = "com.wjl.blog.mapper")
@EnableCaching
@SpringBootApplication
public class DblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DblogApplication.class, args);
    }

}
