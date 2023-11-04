package com.microsimu.internalGateway.auth;

import com.microsimu.internalGateway.auth.dto.request.UserCredentialsRequestDto;
import com.microsimu.internalGateway.infrastructure.apiHandler.ResponseBuilder;
import com.microsimu.internalGateway.infrastructure.config.security.interceptor.ServiceRequestDto;
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
	public ResponseEntity<?> register(@RequestBody UserCredentialsRequestDto dto) {
		dto.setRole("STAFF");
		return restTemplate.postForEntity(authServiceApi + "/register", dto, Object.class);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserCredentialsRequestDto dto) {
		dto.setRole("STAFF");
		return restTemplate.postForEntity(authServiceApi + "/login", dto, Object.class);
	}

	@PostMapping("/register-service")
	public ResponseEntity<?> registerService(@RequestBody ServiceRequestDto dto) {
		return restTemplate.postForEntity(authServiceApi + "/register-service", dto, Object.class);
	}
}
