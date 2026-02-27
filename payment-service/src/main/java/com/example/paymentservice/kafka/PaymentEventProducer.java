package com.example.paymentservice.kafka;

import com.example.paymentservice.dto.PaymentProcessedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class PaymentEventProducer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventProducer.class);

    @Autowired
    private KafkaTemplate<String, PaymentProcessedEvent> kafkaTemplate;

    public void sendPaymentEvent(PaymentProcessedEvent event, String topic) {
        CompletableFuture<SendResult<String, PaymentProcessedEvent>> future =
                kafkaTemplate.send(topic, event.getOrderId(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Payment event sent to topic={}: orderId={}, offset={}",
                        topic, event.getOrderId(), result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send payment event to topic={} for orderId={}",
                        topic, event.getOrderId(), ex);
            }
        });
    }
}
