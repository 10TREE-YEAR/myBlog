package com.wjl.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**)
 * @Description: Swagger2配置类
 * @param @param
 * @return @return
 * @author wangjialu
 * @throws
 * @date 2020/3/17 19:34
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wjl.blog.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    //配置在线文档的基本信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("myBlog使用API")
                .description("将平时学习到的知识运用到自己的博客")
                .termsOfServiceUrl("http://localhost:1314/")
                .version("1.0")
                .build();
    }

}
