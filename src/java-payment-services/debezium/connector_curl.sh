curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @register-postgres.json

#list topics
#bin/kafka-topics.sh --list --bootstrap-server 0.0.0.0:9092

#topic consumer
#bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic iku.public.payment_events

