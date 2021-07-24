package com.aiun.common.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author X21147
 * @Description
 * @date 2021/7/17 14:40
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 基本信息的配置，信息会在 Api 文档上显示
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("订单管理系统测试接口文档")
                .description("订单管理系统所有子系统相关的测试接口文档")
                .termsOfServiceUrl("http://localhost:8084")
                .version("1.0")
                .build();
    }
}
