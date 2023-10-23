package com.microsimu.productService.service.impl;

import com.microsimu.productService.common.constants.AppMessage;
import com.microsimu.productService.dto.request.ProductRequestDto;
import com.microsimu.productService.entity.ProductEntity;
import com.microsimu.productService.mapper.ProductMapper;
import com.microsimu.productService.repository.ProductRepository;
import com.microsimu.productService.repository.ProductSagaRepository;
import com.microsimu.productService.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service(value = "ProductServiceImpl")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	protected final ProductSagaRepository productSagaRepository;
	protected final ProductRepository productRepository;

	@Override
	public ProductEntity createProduct(ProductRequestDto productRequestDto) {
		ProductEntity entity = ProductMapper.toEntityFromRequestDto(productRequestDto);
		return productRepository.save(entity);
	}

	@Override
	public ProductEntity updateProduct(ProductRequestDto productRequestDto) {
		getProductById(productRequestDto.getId());

		ProductEntity updated = ProductMapper.toEntityFromRequestDto(productRequestDto);
		updated.setId(productRequestDto.getId());
		return productRepository.save(updated);	}

	protected ProductEntity getProductById(String id) {
		return productRepository.findById(id).orElseThrow(
				() -> new RuntimeException(MessageFormat.format(AppMessage.NOT_EXISTS, "Product id: " + id))
		);
	}
}
