package com.microsimu.authService.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginResponse {
	private String username;
	private String accessToken;
	private String refreshToken;
}
