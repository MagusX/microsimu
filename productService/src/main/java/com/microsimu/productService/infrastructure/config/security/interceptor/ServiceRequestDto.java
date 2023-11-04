package com.microsimu.productService.infrastructure.config.security.interceptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestDto {
	private String name;
	private String secret;
}
