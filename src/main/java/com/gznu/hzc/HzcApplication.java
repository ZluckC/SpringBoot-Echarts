package com.gznu.hzc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gznu.hzc.mapper")
public class HzcApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzcApplication.class, args);
    }

}
