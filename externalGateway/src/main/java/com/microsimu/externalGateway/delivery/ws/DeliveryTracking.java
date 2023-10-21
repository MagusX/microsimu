package com.microsimu.externalGateway.delivery.ws;

import com.microsimu.externalGateway.infrastructure.config.kafka.KafkaMessage;
import com.microsimu.externalGateway.mapper.PojoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryTracking {
	private PojoMapper pojoMapper;
	private final SimpMessageSendingOperations simpMessagingTemplate;

	@KafkaListener(groupId = "${spring.kafka.consumergroup}", topics = "${ktopic.delivery-tracking-monitor.name}")
	public void listen(ConsumerRecord<String, KafkaMessage> record) {
		String orderId = record.key();
		Object payload = record.value().getPayload();

		simpMessagingTemplate.convertAndSend("/customer/order/" + orderId, payload);
	}
}
