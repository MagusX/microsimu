package com.microsimu.paymentService.kafka;

import com.microsimu.paymentService.common.constants.AppMessage;
import com.microsimu.paymentService.dto.request.PaymentRequestDto;
import com.microsimu.paymentService.dto.request.RefundRequestDto;
import com.microsimu.paymentService.infrastructure.config.kafka.KafkaBlockingMessage;
import com.microsimu.paymentService.mapper.PojoMapper;
import com.microsimu.paymentService.service.PaymentService;
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
	private PaymentService paymentService;

	@KafkaListener(groupId = "${spring.kafka.consumergroup}", topics = "${ktopic.payment-request.name}")
	@SendTo
	public KafkaBlockingMessage listen(ConsumerRecord<String, KafkaBlockingMessage> record) {
		KafkaBlockingMessage request = record.value();
		Object payload = request.getPayload();

		try {
			Object responsePayload = null;
			switch (request.getAction()) {
				case Action.VERIFY_PAYMENT:
					responsePayload = paymentService.verifyPayment(pojoMapper.get(payload, PaymentRequestDto.class));
					break;
				default:
					throw new RuntimeException(MessageFormat.format(AppMessage.SERVER_INVALID_ACTION, request.getPayload()));
			}

			return new KafkaBlockingMessage(request.getRequestId(), request.getAction(), responsePayload);
		} catch (Exception e) {
			return new KafkaBlockingMessage(request.getRequestId(), request.getAction(), e);
		}
	}

	@KafkaListener(groupId = "${spring.kafka.consumergroup}", topics = "${ktopic.rb-payment.name}")
	public void listenAsync(ConsumerRecord<String, KafkaBlockingMessage> record) {
		KafkaBlockingMessage request = record.value();
		Object payload = request.getPayload();

		try {
			switch (request.getAction()) {
				case Action.REFUND:
					paymentService.refund(pojoMapper.get(payload, RefundRequestDto.class));
					break;
				default:
					throw new RuntimeException(MessageFormat.format(AppMessage.SERVER_INVALID_ACTION, request.getPayload()));
			}

		} catch (Exception e) {
			log.error("ERROR: ", e);
		}
	}
}
