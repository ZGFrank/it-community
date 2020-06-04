package com.zgf.itc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zgf.itc.mapper")
public class ItCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItCommunityApplication.class, args);
    }

}
