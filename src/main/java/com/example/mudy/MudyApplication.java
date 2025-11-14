package com.example.mudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MudyApplication.class, args);
    }

}
