package com.microsimu.productService.saga;

import com.microsimu.productService.dto.request.TakeStockSagaDto;
import com.microsimu.productService.entity.ProductEntity;
import com.microsimu.productService.entity.ProductSaga;
import com.microsimu.productService.repository.ProductRepository;
import com.microsimu.productService.repository.ProductSagaRepository;
import com.microsimu.productService.service.impl.ProductServiceImpl;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(value = "ProductServiceSaga")
public class ProductServiceSaga extends ProductServiceImpl {
	public ProductServiceSaga(ProductRepository productRepository, ProductSagaRepository productSagaRepository) {
		super(productSagaRepository, productRepository);
	}

	@Transactional
	public void takeStock(TakeStockSagaDto dto) {
		Map<String, Integer> productIdQuantity = new HashMap<>();
		for (TakeStockSagaDto.ProductDto productDto : dto.getProducts()) {
			productIdQuantity.put(productDto.getId(), productDto.getQuantity());
		}

		List<ProductEntity> products = productRepository.findAllById(productIdQuantity.keySet());
		List<ProductEntity> updatedProducts = new ArrayList<>();
		List<ProductSaga> productSagas = new ArrayList<>();
		for (ProductEntity productEntity : products) {
			int changeQuantity = productIdQuantity.get(productEntity.getId());
			productEntity.takeStock(changeQuantity);
			updatedProducts.add(productEntity);

			ProductSaga productSaga = new ProductSaga();
			productSaga.setTransactionId(dto.getTransactionId());
			productSaga.setProductId(productEntity.getId());
			productSaga.setQuantity(changeQuantity);
			productSagas.add(productSaga);
		}

		productRepository.saveAll(updatedProducts);
		productSagaRepository.saveAll(productSagas);
	}

	@Transactional
	public void takeStockUndo(TakeStockSagaDto dto) {
		List<ProductSaga> productSagas = productSagaRepository.findAllByTransactionId(dto.getTransactionId());
		Map<String, Integer> productIdQuantity = new HashMap<>();
		for (ProductSaga saga : productSagas) {
			productIdQuantity.put(saga.getProductId(), saga.getQuantity());
		}

		List<ProductEntity> products = productRepository.findAllById(productIdQuantity.keySet());
		List<ProductEntity> updatedProducts = new ArrayList<>();
		for (ProductEntity productEntity : products) {
			int changeQuantity = productIdQuantity.get(productEntity.getId());
			productEntity.addStock(changeQuantity);
			updatedProducts.add(productEntity);
		}

		productRepository.saveAll(updatedProducts);
		productSagaRepository.deleteAll(productSagas);
	}
}
