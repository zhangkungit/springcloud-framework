package com.springcloud.framework.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author zhangkun
 * @version 1.0
 * @date 2018/3/5
 * @description
 */
@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
//boot admin
//@EnableAdminServer
public class EurekaApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(EurekaApplication.class);
    }


    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
