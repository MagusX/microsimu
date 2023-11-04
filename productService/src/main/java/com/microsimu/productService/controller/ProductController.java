package com.microsimu.productService.controller;

import com.microsimu.productService.dto.request.ProductRequestDto;
import com.microsimu.productService.entity.ProductEntity;
import com.microsimu.productService.infrastructure.apiHandler.ResponseBuilder;
import com.microsimu.productService.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController {
	@Qualifier(value = "ProductServiceImpl")
	@Autowired
	private ProductService productService;

	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody ProductRequestDto dto) {
		ProductEntity productEntity = productService.createProduct(dto);
		return ResponseBuilder.buildOkResponse(productEntity);
	}
}
