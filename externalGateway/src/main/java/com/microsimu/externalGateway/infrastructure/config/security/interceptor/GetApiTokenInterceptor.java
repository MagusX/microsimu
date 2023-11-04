package com.microsimu.externalGateway.infrastructure.config.security.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetApiTokenInterceptor implements ClientHttpRequestInterceptor {
	private final RestTemplate restTemplate;
	@Value("${microsimu.api.auth-service}")
	private String authServiceApi;
	@Value("${microsimu.service.name}")
	private String serviceName;
	@Value("${microsimu.service.secret}")
	private String serviceSecret;

	private static final List<String> WHITELIST = List.of(
			"/api/auth"
	);

	public GetApiTokenInterceptor(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		request.getHeaders().add("name", serviceName);
		request.getHeaders().add("secret", serviceSecret);

		if (!skipGetApiToken(request)) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("name", serviceName);
			headers.add("secret", serviceSecret);
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(new LinkedHashMap<>(), headers);

			ResponseEntity<ServiceResponseDto> response = restTemplate.exchange(authServiceApi + "/get-api-token", HttpMethod.POST, requestEntity, ServiceResponseDto.class);			if (response.getStatusCodeValue() == 200 && response.getBody() != null) {
				String apiToken = response.getBody().getApiToken();
				request.getHeaders().set("api_token", apiToken);
			} else {
				throw new RuntimeException("Failed to obtain api token: " + response.getHeaders().get("message"));
			}
		}

		return execution.execute(request, body);
	}

	private boolean skipGetApiToken(HttpRequest request) {
		String requestUrl = request.getURI().toString();

		for (String path : WHITELIST) {
			if (requestUrl.contains(path)) {
				return true;
			}
		}
		return false;
	}
}
