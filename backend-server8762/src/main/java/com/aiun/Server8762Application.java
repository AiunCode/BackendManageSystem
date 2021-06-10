package com.aiun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka 服务注册中心
 * @author lenovo
 */
@EnableEurekaServer
@SpringBootApplication
public class Server8762Application {

    public static void main(String[] args) {
        SpringApplication.run(Server8762Application.class, args);
    }

}
