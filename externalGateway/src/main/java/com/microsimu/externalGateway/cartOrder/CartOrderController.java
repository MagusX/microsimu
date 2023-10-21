package com.microsimu.externalGateway.cartOrder;

import com.microsimu.externalGateway.cartOrder.dto.request.CartItemsRequestDto;
import com.microsimu.externalGateway.cartOrder.dto.request.CartRequestDto;
import com.microsimu.externalGateway.cartOrder.dto.request.CreateOrderRequestDto;
import com.microsimu.externalGateway.cartOrder.dto.request.OrderRequestDto;
import com.microsimu.externalGateway.infrastructure.apiHandler.ResponseBuilder;
import com.microsimu.externalGateway.kafka.KafkaMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cart-order")
@RequiredArgsConstructor
public class CartOrderController {
	private final KafkaMessageService kafkaMessageService;

	@Value("${ktopic.cartorder-request.name}")
	private String topicCartOrderRequestName;

	@Value("${ktopic.cartorder-reply.name}")
	private String topicCartOrderReplyName;

	@PostMapping("/get-cart")
	public ResponseEntity<?> getCart(@RequestBody CartRequestDto dto) {
		return ResponseBuilder.buildOkResponse(
				kafkaMessageService.sendBlocking(topicCartOrderRequestName, topicCartOrderReplyName, Action.GET_CART, dto));
	}

	@PostMapping("/add-to-cart")
	public ResponseEntity<?> addToCart(@RequestBody CartItemsRequestDto dto) {
		return ResponseBuilder.buildOkResponse(
				kafkaMessageService.sendBlocking(topicCartOrderRequestName, topicCartOrderReplyName, Action.ADD_TO_CART, dto));
	}

	@PostMapping("/get-order")
	public ResponseEntity<?> getOrder(@RequestBody OrderRequestDto dto) {
		return ResponseBuilder.buildOkResponse(
				kafkaMessageService.sendBlocking(topicCartOrderRequestName, topicCartOrderReplyName, Action.GET_ORDER, dto));
	}

	@PostMapping("/create-order")
	public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequestDto dto) {
		return ResponseBuilder.buildOkResponse(
				kafkaMessageService.sendBlocking(topicCartOrderRequestName, topicCartOrderReplyName, Action.CREATE_ORDER, dto));
	}

	@PutMapping("/cancel-order")
	public ResponseEntity<?> cancelOrder(@RequestBody CreateOrderRequestDto dto) {
		return ResponseBuilder.buildOkResponse(
				kafkaMessageService.sendBlocking(topicCartOrderRequestName, topicCartOrderReplyName, Action.CANCEL_ORDER, dto));
	}
}
