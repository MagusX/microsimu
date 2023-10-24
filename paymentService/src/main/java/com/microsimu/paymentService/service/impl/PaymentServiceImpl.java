package com.microsimu.paymentService.service.impl;

import com.microsimu.paymentService.common.constants.AppMessage;
import com.microsimu.paymentService.dto.request.DeliveryRequestDto;
import com.microsimu.paymentService.dto.request.PaymentRequestDto;
import com.microsimu.paymentService.dto.request.ProductTakeStockSagaDto;
import com.microsimu.paymentService.dto.request.RefundRequestDto;
import com.microsimu.paymentService.entity.PaymentEntity;
import com.microsimu.paymentService.infrastructure.config.kafka.KafkaBlockingMessage;
import com.microsimu.paymentService.kafka.Action;
import com.microsimu.paymentService.kafka.RequestReplyProducer;
import com.microsimu.paymentService.mapper.PaymentMapper;
import com.microsimu.paymentService.repository.PaymentRepository;
import com.microsimu.paymentService.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	@Value("${ktopic.product-request.name}")
	private String topicProductRequestName;
	@Value("${ktopic.payment-product-reply.name}")
	private String topicPaymentProductReplyName;
	@Value("${ktopic.delivery-request.name}")
	private String topicDeliveryRequestName;
	@Value("${ktopic.payment-delivery-reply.name}")
	private String topicPaymentDeliveryReplyName;
	private final RequestReplyProducer requestReplyProducer;
	private final PaymentRepository paymentRepository;

	@Override
	public PaymentEntity verifyPayment(PaymentRequestDto requestDto) {
		if (requestDto.getValue() <= 0) {
			throw new RuntimeException(MessageFormat.format(AppMessage.ACCOUNT_BALANCE_INSUFFICIENT, requestDto.getOrderId()));
		}

		if ("IN".equals(requestDto.getSide())) {
			throw new RuntimeException(MessageFormat.format(AppMessage.INVALID_PARAM, "side: IN"));
		}

		PaymentEntity paymentEntity = paymentRepository.findByOrderId(requestDto.getOrderId()).orElse(null);
		if (paymentEntity != null) {
			throw new RuntimeException(MessageFormat.format(AppMessage.ALREADY_EXISTS, "OrderId: " + requestDto.getOrderId()));
		}

		PaymentEntity entity = PaymentMapper.toEntityFromDto(requestDto);
		entity.setTransactionId(UUID.randomUUID().toString());
		entity.setTimestamp(LocalDateTime.now());

		// create delivery
		DeliveryRequestDto deliveryRequestDto = new DeliveryRequestDto();
		deliveryRequestDto.setTransactionId(entity.getTransactionId());
		deliveryRequestDto.setOrderId(requestDto.getOrderId());
		deliveryRequestDto.setDistance(requestDto.getDistance());

		requestReplyProducer.sendBlocking(topicDeliveryRequestName, topicPaymentDeliveryReplyName, Action.CREAT_DELIVERY, deliveryRequestDto);

		// take stock
		ProductTakeStockSagaDto productTakeStockSagaDto = new ProductTakeStockSagaDto();
		productTakeStockSagaDto.setTransactionId(entity.getTransactionId());
		productTakeStockSagaDto.setProducts(requestDto.getProducts());

		requestReplyProducer.sendBlocking(topicProductRequestName, topicPaymentProductReplyName, Action.TAKE_STOCK, productTakeStockSagaDto);

		return paymentRepository.save(entity);
	}

	@Override
	public void refund(RefundRequestDto requestDto) {
		PaymentEntity paymentEntity = paymentRepository.findByOrderId(requestDto.getOrderId()).orElseThrow(
				() -> new RuntimeException(MessageFormat.format(AppMessage.NOT_EXISTS, "OrderId: " + requestDto.getOrderId()))
		);

		paymentEntity.setRefunded(true);
		paymentEntity.setRefundedAt(LocalDateTime.now());
		paymentRepository.save(paymentEntity);
	}
}
