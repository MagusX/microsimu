package com.microsimu.authService.service;

import com.microsimu.authService.dto.request.UserCredentialsRequestDto;
import com.microsimu.authService.dto.response.LoginResponse;

public interface AuthService {
	LoginResponse authenticateLogin(UserCredentialsRequestDto loginDto);
}
