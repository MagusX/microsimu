package com.microsimu.deliveryService.consumer;

import com.microsimu.deliveryService.common.constants.AppMessage;
import com.microsimu.deliveryService.dto.request.DeliveryRequestDto;
import com.microsimu.deliveryService.infrastructure.config.kafka.KafkaBlockingMessage;
import com.microsimu.deliveryService.mapper.PojoMapper;
import com.microsimu.deliveryService.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Slf4j
@Component
public class RequestReplyConsumer {
	@Autowired
	private PojoMapper pojoMapper;
	@Autowired
	private DeliveryService deliveryService;

	@KafkaListener(groupId = "${spring.kafka.consumergroup}", topics = "${ktopic.delivery-request.name}")
	@SendTo
	public KafkaBlockingMessage listen(ConsumerRecord<String, KafkaBlockingMessage> record) {
		KafkaBlockingMessage request = record.value();
		Object payload = request.getPayload();

		try {
			Object responsePayload = null;
			switch (request.getAction()) {
				case Action.CREATE_DELIVERY:
					deliveryService.createDelivery(pojoMapper.get(payload, DeliveryRequestDto.class));
					break;
				default:
					throw new RuntimeException(MessageFormat.format(AppMessage.SERVER_INVALID_ACTION, request.getPayload()));
			}

			return new KafkaBlockingMessage(request.getRequestId(), request.getAction(), responsePayload);
		} catch (Exception e) {
			return new KafkaBlockingMessage(request.getRequestId(), request.getAction(), e);
		}
	}
}
