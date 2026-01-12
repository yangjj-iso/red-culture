package com.redculture.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.redculture.backend.mapper")
public class RedCultureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedCultureBackendApplication.class, args);
    }

}
