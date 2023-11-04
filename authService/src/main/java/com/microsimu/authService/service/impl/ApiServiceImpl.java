package com.microsimu.authService.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.microsimu.authService.common.Utils;
import com.microsimu.authService.common.constants.AppMessage;
import com.microsimu.authService.dto.request.ServiceRequestDto;
import com.microsimu.authService.dto.response.ServiceResponseDto;
import com.microsimu.authService.entity.ServiceEntity;
import com.microsimu.authService.repository.ServiceRepository;
import com.microsimu.authService.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {
	@Value("${app.api.secret}")
	private String apiSecret;
	private final ServiceRepository serviceRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void registerService(ServiceRequestDto dto) {
		if (serviceRepository.findByName(dto.getName()).isPresent()) {
			throw new RuntimeException(MessageFormat.format(AppMessage.SERVER_INVALID_ACTION, "Service already saved " + dto.getName()));
		}

		String salt = Utils.generateRandomString(5);

		ServiceEntity service = new ServiceEntity();
		service.setName(dto.getName());
		service.setSecret(passwordEncoder.encode(dto.getSecret() + salt));
		service.setSalt(salt);
		serviceRepository.save(service);
	}

	@Override
	public ServiceResponseDto generateApiToken(ServiceRequestDto dto) {
		LocalDateTime exp = LocalDateTime.now().plusMinutes(5);
		String token = JWT.create()
				.withExpiresAt(exp.toInstant(ZoneId.systemDefault().getRules().getOffset(exp)))
				.withIssuer("microsimu")
				.sign(Algorithm.HMAC512(apiSecret.getBytes()));

		return new ServiceResponseDto(token);
	}
}
