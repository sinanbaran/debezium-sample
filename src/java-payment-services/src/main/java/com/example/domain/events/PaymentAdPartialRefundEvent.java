package com.example.domain.events;

import com.example.core.DomainEvent;


public class PaymentAdPartialRefundEvent extends DomainEvent {
    private final float amount;

    public PaymentAdPartialRefundEvent(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }
}
