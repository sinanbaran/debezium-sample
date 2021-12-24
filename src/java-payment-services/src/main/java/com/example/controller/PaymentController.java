package com.example.controller;

import com.example.domain.PaymentAggregate;
import com.example.repository.PaymentAggregateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PaymentController {

    private final PaymentAggregateRepository paymentAggregateRepository;

    public PaymentController(PaymentAggregateRepository paymentAggregateRepository) {
        this.paymentAggregateRepository = paymentAggregateRepository;
    }

    @GetMapping("/payments")
    public String test() {
        return paymentAggregateRepository.test();
    }

    @PostMapping("/payments/pay")
    public String pay() throws JsonProcessingException {
        PaymentAggregate aggregate = new PaymentAggregate(UUID.randomUUID(), 100);
        aggregate.AddTransaction("init");
        aggregate.AddTransaction("text");
        aggregate.Complete();

        paymentAggregateRepository.SaveChanges(aggregate.getChanges());
        return "complete";
    }
}
