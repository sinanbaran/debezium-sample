package com.example.domain;

import com.example.core.Aggregate;
import com.example.core.DomainEvent;
import com.example.domain.events.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class PaymentAggregate extends Aggregate {
    private List<PaymentTransaction> transactions;
    private float amount;
    private String state;

    public PaymentAggregate(List<DomainEvent> events) {
        Load(events);
    }

    public PaymentAggregate(UUID aggregateId, float amount) {
        setAggregateId(aggregateId);
        applyChange(new PaymentCreatedEvent(amount));
        applyChange(new PaymentContinueEvent());
    }

    public void Complete() {
        applyChange(new PaymentCompletedEvent());
    }

    public void Cancel() {
        applyChange(new PaymentCancelledEvent());
    }

    public void AddPartialRefund(float refundAmount) {
        applyChange(new PaymentAdPartialRefundEvent(refundAmount));
    }

    public void Refund() {
        applyChange(new RefundPaymentEvent());
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
        transactions.add(new PaymentTransaction(event.content()));
    }

    public void apply(PaymentCancelledEvent event) {

        this.state = "cancel";
    }

    public void apply(PaymentCompletedEvent event) {

        this.state = "complete";
    }

    public void apply(RefundPaymentEvent event) {
        this.state = "refunded";
    }

    public void apply(PaymentContinueEvent event) {
        this.state = "continue";
    }

}
