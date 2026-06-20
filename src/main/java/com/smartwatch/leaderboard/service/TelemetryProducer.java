package com.smartwatch.leaderboard.service;

import com.smartwatch.leaderboard.dto.TelemetryMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TelemetryProducer {

    private static final String TOPIC = "smartwatch-telemetry";
    private final KafkaTemplate<String, TelemetryMessage> kafkaTemplate;
    private final TelemetryConsumer telemetryConsumer;
    private final boolean kafkaEnabled;

    // Inject KafkaTemplate, TelemetryConsumer and the kafka.enabled flag
    public TelemetryProducer(
            KafkaTemplate<String, TelemetryMessage> kafkaTemplate,
            TelemetryConsumer telemetryConsumer,
            @Value("${kafka.enabled:false}") boolean kafkaEnabled
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.telemetryConsumer = telemetryConsumer;
        this.kafkaEnabled = kafkaEnabled;
    }

    public void sendTelemetryEvent(TelemetryMessage message) {
        if (kafkaEnabled) {
            System.out.println(">>> [KAFKA MODE] Publishing telemetry message to Kafka topic.");
            kafkaTemplate.send(TOPIC, message.getUserId().toString(), message);
        } else {
            System.out.println(">>> [KAFKA MOCK MODE] Directly delivering telemetry message to consumer in-memory.");
            // Under mock mode, we bypass the physical broker and call the consumer directly!
            // This allows the whole application to work 100% end-to-end with ZERO setup!
            telemetryConsumer.consumeTelemetryEvent(message);
        }
    }
}
