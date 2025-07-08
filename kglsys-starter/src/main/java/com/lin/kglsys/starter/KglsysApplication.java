package com.lin.kglsys.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 系统主启动类
 * @author YourName (Backend Architect)
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.lin.kglsys"})
@EnableJpaRepositories(basePackages = {"com.lin.kglsys.infra.repository"})
@EntityScan(basePackages = {"com.lin.kglsys.domain"})
public class KglsysApplication {

    public static void main(String[] args) {
        SpringApplication.run(KglsysApplication.class, args);
    }

}