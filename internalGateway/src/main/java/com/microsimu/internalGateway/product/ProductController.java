package com.microsimu.internalGateway.product;

import com.microsimu.internalGateway.infrastructure.apiHandler.ResponseBuilder;
import com.microsimu.internalGateway.kafka.KafkaMessageService;
import com.microsimu.internalGateway.product.dto.request.ProductRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
	private final KafkaMessageService kafkaMessageService;

	@Value("${ktopic.product-request.name}")
	private String topicProductRequestName;

	@Value("${ktopic.product-reply.name}")
	private String topicProductReplyName;

	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody ProductRequestDto dto) {
		return ResponseBuilder.buildOkResponse(
				kafkaMessageService.sendBlocking(topicProductRequestName, topicProductReplyName, Action.CREATE_PRODUCT, dto));
	}
}
