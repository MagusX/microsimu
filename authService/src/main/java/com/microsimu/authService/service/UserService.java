package com.microsimu.authService.service;

import com.microsimu.authService.dto.request.UserCredentialsRequestDto;
import com.microsimu.authService.dto.response.LoginResponse;
import com.microsimu.authService.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
	UserEntity createUser(UserCredentialsRequestDto requestDto);
}
