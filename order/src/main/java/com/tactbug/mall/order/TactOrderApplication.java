package com.tactbug.mall.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class TactOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(TactOrderApplication.class, args);
    }
}
