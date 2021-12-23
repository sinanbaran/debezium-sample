package com.example.core;

import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent {
    protected long id;
    protected UUID aggregateId;
    protected int version;
    protected Instant createdDate = Instant.now();


    public DomainEvent() {
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public UUID getAggregateId() {
        return this.aggregateId;
    }

    public String getEventType() {
        return this.getClass().getSimpleName();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public long getId() {
        return this.id;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }
}
