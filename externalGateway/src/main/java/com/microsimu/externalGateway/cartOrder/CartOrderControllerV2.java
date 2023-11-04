package com.microsimu.externalGateway.cartOrder;

import com.microsimu.externalGateway.cartOrder.dto.request.CartItemsRequestDto;
import com.microsimu.externalGateway.cartOrder.dto.request.CartRequestDto;
import com.microsimu.externalGateway.cartOrder.dto.request.CreateOrderRequestDto;
import com.microsimu.externalGateway.cartOrder.dto.request.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/v2/cart-order")
@RequiredArgsConstructor
public class CartOrderControllerV2 {
	private final RestTemplate restTemplate;

	@Value("${microsimu.api.cart-order-service}")
	private String cartOrderServiceApi;

	@PostMapping("/get-cart")
	public ResponseEntity<?> getCart(@RequestBody CartRequestDto dto) {
		return restTemplate.postForEntity(cartOrderServiceApi + "/get-cart", dto, Object.class);
	}

	@PostMapping("/add-to-cart")
	public ResponseEntity<?> addToCart(@RequestBody CartItemsRequestDto dto) {
		return restTemplate.postForEntity(cartOrderServiceApi + "/add-to-cart", dto, Object.class);
	}

	@PostMapping("/get-order")
	public ResponseEntity<?> getOrder(@RequestBody OrderRequestDto dto) {
		return restTemplate.postForEntity(cartOrderServiceApi + "/get-order", dto, Object.class);
	}

	@PostMapping("/create-order")
	public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequestDto dto) {
		return restTemplate.postForEntity(cartOrderServiceApi + "/create-order", dto, Object.class);
	}
}
