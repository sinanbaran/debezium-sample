package com.example;

import com.example.domain.PaymentAggregate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@SpringBootApplication
@EnableScheduling
@RestController
public class MainApplication {
    public MainApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @GetMapping("/payments")
    public String test() {
        return "lets go";
    }

    @PostMapping("/payments")

    public String pay() {

        PaymentAggregate aggregate = new PaymentAggregate(UUID.randomUUID(), 100);
        aggregate.AddTransaction("init");
        aggregate.AddTransaction("text");
        aggregate.AddPartialRefund(10);
        aggregate.Complete();
        aggregate.Cancel();

        return "";
    }
}
