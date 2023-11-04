package com.microsimu.internalGateway.product;

import com.microsimu.internalGateway.product.dto.request.ProductRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/v2/product")
@RequiredArgsConstructor
public class ProductControllerV2 {
	@Value("${microsimu.api.product-service}")
	private String productServiceApi;
	private final RestTemplate restTemplate;

	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody ProductRequestDto dto) {
		return restTemplate.postForEntity(productServiceApi + "/create", dto, Object.class);
	}
}
