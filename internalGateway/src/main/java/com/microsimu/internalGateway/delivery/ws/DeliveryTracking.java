package com.microsimu.internalGateway.delivery.ws;

import com.microsimu.internalGateway.common.mapper.PojoMapper;
import com.microsimu.internalGateway.infrastructure.config.kafka.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

		Map<String, Object> wsMessage = new LinkedHashMap<>();
		wsMessage.put(orderId, payload);

		simpMessagingTemplate.convertAndSend("/admin/delivery", wsMessage);
	}
}
