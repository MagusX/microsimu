package com.microsimu.internalGateway.infrastructure.config.http;

import com.microsimu.internalGateway.infrastructure.config.security.interceptor.GetApiTokenInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {
	@Bean
	public GetApiTokenInterceptor getApiTokenInterceptor() {
		return new GetApiTokenInterceptor(new RestTemplate());
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		restTemplate.setInterceptors(List.of(getApiTokenInterceptor()));

		return restTemplate;
	}
}
