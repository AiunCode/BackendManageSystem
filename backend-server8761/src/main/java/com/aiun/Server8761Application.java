package com.aiun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 * 注册服务启动类
 * @author lenovo
 */
@EnableEurekaServer
@SpringBootApplication
public class Server8761Application {

    public static void main(String[] args) {
        SpringApplication.run(Server8761Application.class, args);
    }

}
