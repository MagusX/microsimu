package com.microsimu.authService.service;

import com.microsimu.authService.dto.request.ServiceRequestDto;
import com.microsimu.authService.dto.response.ServiceResponseDto;

public interface ApiService {
	void registerService(ServiceRequestDto dto);

	ServiceResponseDto generateApiToken(ServiceRequestDto dto);
}
