package com.microsimu.externalGateway.auth;

import com.microsimu.externalGateway.auth.dto.request.UserCredentialsRequestDto;
import com.microsimu.externalGateway.cartOrder.dto.request.CartItemsRequestDto;
import com.microsimu.externalGateway.cartOrder.dto.request.CartRequestDto;
import com.microsimu.externalGateway.cartOrder.dto.request.CreateOrderRequestDto;
import com.microsimu.externalGateway.cartOrder.dto.request.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final RestTemplate restTemplate;

	@Value("${microsimu.api.auth-service}")
	private String authServiceApi;

	@PostMapping("/register")
	public ResponseEntity<?> getCart(@RequestBody UserCredentialsRequestDto dto) {
		dto.setRole("CUSTOMER");
		return restTemplate.postForEntity(authServiceApi + "/register", dto, Object.class);
	}

	@PostMapping("/login")
	public ResponseEntity<?> addToCart(@RequestBody UserCredentialsRequestDto dto) {
		dto.setRole("CUSTOMER");
		return restTemplate.postForEntity(authServiceApi + "/login", dto, Object.class);
	}
}
