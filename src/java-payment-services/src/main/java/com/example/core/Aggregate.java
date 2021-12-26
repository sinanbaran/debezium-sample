package com.example.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
public class Aggregate {
    protected final List<DomainEvent> changes = new ArrayList<>();
    protected UUID aggregateId;
    protected int version = 0;


    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    protected void Load(List<DomainEvent> events) {
        events.forEach(
                event -> {
                    apply(event);
                    version = event.getVersion();
                });
    }

    protected void applyChange(DomainEvent event) {

        event.setAggregateId(this.getAggregateId());
        event.setVersion(getNextVersion());

        if (event.getVersion() != getNextVersion()) {
            throw new IllegalStateException(
                    String.format("Event version %s doesn't match expected version %s", event.getVersion(), getNextVersion()));
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
        return version + changes.size() + 1;
    }
}
