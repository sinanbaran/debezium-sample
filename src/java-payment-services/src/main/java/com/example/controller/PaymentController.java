package com.example.controller;

import com.example.domain.PaymentAggregate;
import com.example.repository.PaymentAggregateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/payments/{aggregateId}")
    public PaymentAggregate get(@PathVariable UUID aggregateId) {
        PaymentAggregate paymentAggregate = paymentAggregateRepository.Get(aggregateId);
        return paymentAggregate;
    }

    @PatchMapping("/payments/{aggregateId}/complete")
    public PaymentAggregate complete(@PathVariable UUID aggregateId) throws JsonProcessingException {
        PaymentAggregate paymentAggregate = paymentAggregateRepository.Get(aggregateId);
        paymentAggregate.Complete();
        paymentAggregateRepository.SaveChanges(paymentAggregate.getChanges());
        return paymentAggregate;
    }
    @PatchMapping("/payments/{aggregateId}/cancel")
    public PaymentAggregate cancel(@PathVariable UUID aggregateId) throws JsonProcessingException {
        PaymentAggregate paymentAggregate = paymentAggregateRepository.Get(aggregateId);
        paymentAggregate.Cancel();
        paymentAggregateRepository.SaveChanges(paymentAggregate.getChanges());
        return paymentAggregate;
    }

    @PostMapping("/payments/pay")
    public String pay() throws JsonProcessingException {
        PaymentAggregate aggregate = new PaymentAggregate(UUID.randomUUID(), 100);
        aggregate.AddPartialRefund(10);
        aggregate.AddPartialRefund(12);
        aggregate.AddPartialRefund(14);
        aggregate.AddPartialRefund(10);
        aggregate.AddPartialRefund(11);
        paymentAggregateRepository.SaveChanges(aggregate.getChanges());
        return aggregate.getAggregateId().toString();
    }

}
