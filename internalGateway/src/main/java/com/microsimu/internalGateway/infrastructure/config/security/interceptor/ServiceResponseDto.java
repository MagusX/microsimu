package com.microsimu.internalGateway.infrastructure.config.security.interceptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponseDto {
	private String apiToken;
}
