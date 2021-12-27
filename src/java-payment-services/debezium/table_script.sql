create table payment_events
(
    id bigserial,
    aggregate_id uuid,
    content json,
    event_type text
);

create unique index payment_events_id_uindex
    on payment_events (id);

alter table payment_events
    add constraint payment_events_pk
        primary key (id);
