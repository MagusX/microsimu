package com.microsimu.externalGateway.product;

import com.microsimu.externalGateway.kafka.KafkaMessageService;
import com.microsimu.externalGateway.product.dto.request.ProductRequestDto;
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
}
