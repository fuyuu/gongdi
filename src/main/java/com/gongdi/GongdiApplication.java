package com.gongdi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gongdi.mapper")
public class GongdiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GongdiApplication.class, args);
    }

}
