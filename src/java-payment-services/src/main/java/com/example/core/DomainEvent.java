package com.example.core;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public abstract class DomainEvent {
    protected long id;
    protected UUID aggregateId;
    protected int version;
    protected Instant createdDate = Instant.now();


    public DomainEvent() {
    }
    public String getEventType() {
        return this.getClass().getSimpleName();
    }


}
