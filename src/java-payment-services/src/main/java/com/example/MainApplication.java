package com.example;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableScheduling
@RestController
@RequiredArgsConstructor
public class MainApplication {
    public static void main(String[] args) {

        SpringApplication.run(MainApplication.class, args);
    }
    @GetMapping("/payments")
    public String test() {
        return "lets go";
    }
}
