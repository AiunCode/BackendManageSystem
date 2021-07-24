package com.aiun.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 后台启动类
 * @author lenovo
 */
@EnableSwagger2
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.aiun.product.feign", "com.aiun.user.feign", "com.aiun.shipping.feign"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
