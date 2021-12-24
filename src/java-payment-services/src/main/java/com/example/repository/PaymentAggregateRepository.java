package com.example.repository;

import com.example.core.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class PaymentAggregateRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public String test() {
        return "dsds";
    }

    public void SaveChanges(List<DomainEvent> events) throws JsonProcessingException {

        jdbcTemplate.batchUpdate("INSERT INTO payment_events(aggregate_id, content, event_type) VALUES (?,?,?)", new BatchPreparedStatementSetter() {

            @SneakyThrows
            public void setValues(PreparedStatement ps, int i) {
                ps.setObject(1, events.get(i).getAggregateId());
                String jsonContent = objectMapper.writeValueAsString(events.get(i));
                ps.setString(2, jsonContent);
                ps.setString(3, events.get(i).getEventType());
            }

            public int getBatchSize() {
                return events.size();
            }

        });


    }

}
