package com.example.domain;

import com.example.core.Aggregate;
import com.example.domain.events.PaymentAdPartialRefundEvent;
import com.example.domain.events.PaymentCancelledEvent;
import com.example.domain.events.PaymentCompletedEvent;
import com.example.domain.events.PaymentCreatedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PaymentAggregate extends Aggregate {
    private List<PaymentTransaction> transactions;
    private float amount;
    private String stage;

    public PaymentAggregate(UUID aggregateId, float amount) {
        super(aggregateId);
        applyChange(new PaymentCreatedEvent(amount));
    }

    public void Complete() {
        applyChange(new PaymentCompletedEvent());
    }

    public void Cancel() {
        applyChange(new PaymentCancelledEvent());
    }

    public void AddTransaction(String content) {
        applyChange(new PaymentTransaction(content));
    }

    public void AddPartialRefund(float refundAmount) {
        applyChange(new PaymentAdPartialRefundEvent(refundAmount));
    }


    public void apply(PaymentCreatedEvent event) {
        this.setAggregateId(event.getAggregateId());
        this.amount = event.getAmount();
    }

    public void apply(PaymentAdPartialRefundEvent event) {

        if (Objects.isNull(transactions))
            transactions = new ArrayList<>();
        transactions.add(new PaymentTransaction(String.format("refund amount : %s", event.getAmount())));
    }

    public void apply(PaymentTransaction event) {
        if (Objects.isNull(transactions))
            transactions = new ArrayList<>();

        transactions.add(new PaymentTransaction(event.getContent()));
    }

    public void apply(PaymentCancelledEvent event) {
        this.stage = "cancel";
    }

    public void apply(PaymentCompletedEvent event) {
        this.stage = "complete";
    }

}
