package com.example.repository;

import com.example.core.DomainEvent;
import com.example.domain.PaymentAggregate;
import com.example.domain.events.PaymentCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class PaymentAggregateRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public String test() {
        return "dsds";
    }

    public void SaveChanges(List<DomainEvent> events) throws JsonProcessingException {

        BatchPreparedStatementSetter params = new BatchPreparedStatementSetter() {
            @SneakyThrows
            public void setValues(PreparedStatement ps, int i) {
                ps.setObject(1, events.get(i).getAggregateId());
                String stringContent = objectMapper.writeValueAsString(events.get(i));

                PGobject jsonbObj = new PGobject();
                jsonbObj.setType("json");
                jsonbObj.setValue(stringContent);

                ps.setObject(2, jsonbObj);
                ps.setString(3, events.get(i).getEventType());
            }

            public int getBatchSize() {
                return events.size();
            }
        };

        jdbcTemplate.batchUpdate("INSERT INTO payment_events(aggregate_id, content, event_type) VALUES (?,?,?)", params);


    }

    public PaymentAggregate Get(UUID aggregateId) {
        var events = jdbcTemplate.query(
                "SELECT * FROM payment_events WHERE aggregate_id = ? ORDER BY id ASC",
                (rs, rowNum) -> toEvents(rs),
                aggregateId);
        return new PaymentAggregate(events);
    }

    private DomainEvent toEvents(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");

        String eventType = rs.getString("event_type");
        PGobject jsonObj = (PGobject) rs.getObject("content");
        String json = jsonObj.getValue();
        DomainEvent event = null;
        try {

            var da = toClass(eventType);
            event = objectMapper.readValue(json, da);
        } catch (JsonProcessingException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return event;
    }

    private Class<DomainEvent> toClass(String eventType) throws ClassNotFoundException {
        var className = PaymentCreatedEvent.class.getPackageName() + "." + eventType;

        return (Class<DomainEvent>) Class.forName(className);
    }
}
