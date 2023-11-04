package com.microsimu.authService.controller;

import com.microsimu.authService.dto.request.ServiceRequestDto;
import com.microsimu.authService.dto.request.UserCredentialsRequestDto;
import com.microsimu.authService.dto.response.LoginResponse;
import com.microsimu.authService.dto.response.ServiceResponseDto;
import com.microsimu.authService.entity.UserEntity;
import com.microsimu.authService.infrastructure.apiHandler.ResponseBuilder;
import com.microsimu.authService.service.ApiService;
import com.microsimu.authService.service.AuthService;
import com.microsimu.authService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;
	private final AuthService authService;
	private final ApiService apiService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserCredentialsRequestDto dto) {
		UserEntity userEntity = userService.createUser(dto);
		return ResponseBuilder.buildOkResponse(userEntity);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserCredentialsRequestDto dto) {
		LoginResponse response = authService.authenticateLogin(dto);
		return ResponseBuilder.buildOkResponse(response);
	}

	@PostMapping("/register-service")
	public ResponseEntity<?> registerService(@RequestBody ServiceRequestDto dto) {
		apiService.registerService(dto);
		return ResponseBuilder.buildOkResponse();
	}

	@PostMapping("/get-api-token")
	public ResponseEntity<?> getApiToken(@RequestBody ServiceRequestDto dto) {
		ServiceResponseDto response = apiService.generateApiToken(dto);
		return ResponseBuilder.buildOkResponse(response);
	}
}
