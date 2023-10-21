package com.microsimu.productService.service;

import com.microsimu.productService.dto.request.ProductRequestDto;
import com.microsimu.productService.entity.ProductEntity;

public interface ProductService {
	ProductEntity createProduct(ProductRequestDto productRequestDto);

	ProductEntity updateProduct(ProductRequestDto productRequestDto);
}
