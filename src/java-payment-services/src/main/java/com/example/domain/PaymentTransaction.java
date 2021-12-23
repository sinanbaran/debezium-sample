package com.example.domain;

import com.example.core.DomainEvent;

public class PaymentTransaction extends DomainEvent {
    private final String content;

    public PaymentTransaction(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}
