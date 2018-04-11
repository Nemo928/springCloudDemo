package com.zj.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaMasterServerApp
{
    public static void main(String[] args)
    {
        SpringApplication.run(EurekaMasterServerApp.class, args);
    }
}