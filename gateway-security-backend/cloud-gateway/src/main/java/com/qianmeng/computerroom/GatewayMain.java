package com.qianmeng.computerroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * @author qianmeng
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.qianmeng.computerroom.mapper")
@EnableWebFlux
@EnableCaching
@EnableTransactionManagement
public class GatewayMain {
    public static void main(String[] args) {
        SpringApplication.run(GatewayMain.class, args);
    }
}
