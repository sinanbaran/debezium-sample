package com.example.core;

import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Aggregate {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Aggregate.class);
    protected final List<DomainEvent> changes = new ArrayList<>();
    protected UUID aggregateId;
    protected int baseVersion = 0;

    public Aggregate(UUID aggregateId, List<DomainEvent> events) {
        Objects.requireNonNull(aggregateId);
        Objects.requireNonNull(events);
        this.aggregateId = aggregateId;
        loadFromHistory(events);
    }

    public Aggregate(UUID aggregateId) {
        this(aggregateId, Collections.emptyList());
    }

    public Aggregate() {
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    private void loadFromHistory(List<DomainEvent> events) {
        events.forEach(
                event -> {
                    apply(event);
                    baseVersion = event.getVersion();
                });
    }

    protected void applyChange(DomainEvent event) {

        event.setAggregateId(aggregateId);
        event.setVersion(getNextVersion());

        if (event.getVersion() != getNextVersion()) {
            throw new IllegalStateException(
                    String.format(
                            "Event version %s doesn't match expected version %s",
                            event.getVersion(), getNextVersion()));
        }
        apply(event);
        changes.add(event);
    }

    private void apply(DomainEvent event) {
        invoke(event);
    }


    private void invoke(Object o) {
        try {
            Method method = this.getClass().getMethod("apply", o.getClass());
            method.invoke(this, o);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new UnsupportedOperationException(
                    String.format(
                            "Aggregate '%s' doesn't support %s(%s)",
                            this.getClass().getSimpleName(), "apply", o.getClass().getSimpleName()),
                    e);
        }
    }

    protected int getNextVersion() {
        return baseVersion + changes.size() + 1;
    }

    public List<DomainEvent> getChanges() {
        return this.changes;
    }

    public UUID getAggregateId() {
        return this.aggregateId;
    }

    public int getBaseVersion() {
        return this.baseVersion;
    }

    public void setBaseVersion(int baseVersion) {
        this.baseVersion = baseVersion;
    }
}
