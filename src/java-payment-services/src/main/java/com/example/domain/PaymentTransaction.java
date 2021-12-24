package com.example.domain;

import com.example.core.DomainEvent;
import lombok.Getter;

@Getter
public class PaymentTransaction extends DomainEvent {
    private final String content;

    public PaymentTransaction(String content) {
        this.content = content;
    }

}
