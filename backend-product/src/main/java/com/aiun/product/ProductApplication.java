package com.aiun.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 产品启动类
 * @author lenovo
 */
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.aiun.user.feign"})
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
