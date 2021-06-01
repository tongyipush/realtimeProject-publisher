package com.atguigu.gmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.atguigu.gmall.mapper")
public class Gmall0921PublisherApplication {

    public static void main(String[] args) {
        SpringApplication.run(Gmall0921PublisherApplication.class, args);
    }

}
