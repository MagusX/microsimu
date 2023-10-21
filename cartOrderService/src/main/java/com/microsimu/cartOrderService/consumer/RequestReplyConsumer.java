package com.microsimu.cartOrderService.consumer;

import com.microsimu.cartOrderService.common.constants.AppMessage;
import com.microsimu.cartOrderService.dto.request.CartItemsRequestDto;
import com.microsimu.cartOrderService.dto.request.CartRequestDto;
import com.microsimu.cartOrderService.dto.request.CreateOrderRequestDto;
import com.microsimu.cartOrderService.dto.request.OrderRequestDto;
import com.microsimu.cartOrderService.infrastructure.config.kafka.KafkaBlockingMessage;
import com.microsimu.cartOrderService.mapper.PojoMapper;
import com.microsimu.cartOrderService.service.CartService;
import com.microsimu.cartOrderService.service.OrderService;
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
	private CartService cartService;
	@Autowired
	private OrderService orderService;

	@KafkaListener(groupId = "${spring.kafka.consumergroup}", topics = "${ktopic.cartorder-request.name}")
	@SendTo
	public KafkaBlockingMessage listen(ConsumerRecord<String, KafkaBlockingMessage> record) {
		KafkaBlockingMessage request = record.value();
		Object payload = request.getPayload();

		try {
			Object responsePayload = null;
			switch (request.getAction()) {
				case Action.GET_CART:
					responsePayload = cartService.getCartItems(pojoMapper.get(payload, CartRequestDto.class));
					break;
				case Action.ADD_TO_CART:
					cartService.addToCart(pojoMapper.get(payload, CartItemsRequestDto.class));
					break;
				case Action.GET_ORDER:
					responsePayload = orderService.getOrder(pojoMapper.get(payload, OrderRequestDto.class));
					break;
				case Action.CREATE_ORDER:
					responsePayload = orderService.createOrder(pojoMapper.get(payload, CreateOrderRequestDto.class));
					break;
				case Action.CANCEL_ORDER:
					responsePayload = orderService.cancelOrder(pojoMapper.get(payload, OrderRequestDto.class));
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
