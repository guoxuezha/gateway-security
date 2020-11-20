package com.qianmeng.computerroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 郭超
 * Date:2020-08-11 14:18
 * Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.qianmeng.computerroom.mapper")
@EnableSwagger2
@EnableCaching
@EnableTransactionManagement
public class LoginAuthMain {
    public static void main(String[] args) {
        SpringApplication.run(LoginAuthMain.class, args);
    }
}
