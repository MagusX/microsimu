package com.microsimu.productService;

import com.microsimu.productService.dto.request.TakeStockSagaDto;
import com.microsimu.productService.entity.ProductEntity;
import com.microsimu.productService.entity.ProductSaga;
import com.microsimu.productService.mapper.ProductMapper;
import com.microsimu.productService.repository.ProductRepository;
import com.microsimu.productService.repository.ProductSagaRepository;
import com.microsimu.productService.saga.ProductServiceSaga;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
public class ProductServiceSagaTest {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductSagaRepository productSagaRepository;

	private ProductServiceSaga productServiceSaga;

	@BeforeEach
	void init() {
		productServiceSaga = new ProductServiceSaga(productRepository, productSagaRepository);
	}

	@Test
	public void testTakeStock() {
		Map<String, ProductEntity> prodMap = new HashMap<>();

		ProductEntity prod1 = new ProductEntity();
		prod1.setName("prod1");
		prod1.setCategory("CAT");
		prod1.setPrice(10f);
		prod1.setStock(100);
		prodMap.put(prod1.getName(), prod1);

		ProductEntity prod2 = new ProductEntity();
		prod2.setName("prod2");
		prod2.setCategory("CAT");
		prod2.setPrice(10f);
		prod2.setStock(50);
		prodMap.put(prod2.getName(), prod2);

		productRepository.saveAll(prodMap.values());

		String transactionId = UUID.randomUUID().toString();

		TakeStockSagaDto.ProductDto productDto1 = new TakeStockSagaDto.ProductDto();
		productDto1.setId(prod1.getId());
		productDto1.setQuantity(5);

		TakeStockSagaDto.ProductDto productDto2 = new TakeStockSagaDto.ProductDto();
		productDto2.setId(prod2.getId());
		productDto2.setQuantity(5);

		TakeStockSagaDto dto = new TakeStockSagaDto();
		dto.setTransactionId(transactionId);
		dto.setProducts(List.of(productDto1, productDto2));

		productServiceSaga.takeStock(dto);

		String sql = "SELECT * FROM product WHERE name IN ('prod1', 'prod2');";
		Query query = entityManager.createNativeQuery(sql, ProductEntity.class);

		List<ProductEntity> productEntities = query.getResultList();

		Assertions.assertTrue(productEntities.size() == prodMap.values().size());
		for (ProductEntity prod : productEntities) {
			ProductEntity expected = prodMap.get(prod.getName());
			Assertions.assertEquals(expected.getStock(), prod.getStock());
		}

		String sql1 = "SELECT * FROM product_saga WHERE transaction_id = '" + transactionId + "';";
		Query query1 = entityManager.createNativeQuery(sql1, ProductSaga.class);

		List<ProductSaga> productSagas = query1.getResultList();
		Assertions.assertTrue(productSagas.size() == productEntities.size());
		for (ProductSaga saga : productSagas) {
			Assertions.assertEquals(saga.getQuantity(), 5);
		}
	}

	@Test
	public void testTakeStockUndo() {
		Map<String, ProductEntity> prodMap = new HashMap<>();

		ProductEntity prod1 = new ProductEntity();
		prod1.setName("prod1");
		prod1.setCategory("CAT");
		prod1.setPrice(10f);
		prod1.setStock(100);
		prodMap.put(prod1.getName(), prod1);

		ProductEntity prod2 = new ProductEntity();
		prod2.setName("prod2");
		prod2.setCategory("CAT");
		prod2.setPrice(10f);
		prod2.setStock(50);
		prodMap.put(prod2.getName(), prod2);

		productRepository.saveAll(prodMap.values());

		String transactionId = UUID.randomUUID().toString();

		TakeStockSagaDto.ProductDto productDto1 = new TakeStockSagaDto.ProductDto();
		productDto1.setId(prod1.getId());
		productDto1.setQuantity(5);

		TakeStockSagaDto.ProductDto productDto2 = new TakeStockSagaDto.ProductDto();
		productDto2.setId(prod2.getId());
		productDto2.setQuantity(5);

		TakeStockSagaDto dto = new TakeStockSagaDto();
		dto.setTransactionId(transactionId);
		dto.setProducts(List.of(productDto1, productDto2));

		productServiceSaga.takeStock(dto);


		TakeStockSagaDto undoDto = new TakeStockSagaDto();
		undoDto.setTransactionId(transactionId);

		productServiceSaga.takeStockUndo(undoDto);

		String sql = "SELECT * FROM product WHERE name IN ('prod1', 'prod2');";
		Query query = entityManager.createNativeQuery(sql, ProductEntity.class);

		List<ProductEntity> productEntities = query.getResultList();

		Assertions.assertTrue(productEntities.size() == prodMap.values().size());
		for (ProductEntity prod : productEntities) {
			ProductEntity expected = prodMap.get(prod.getName());
			Assertions.assertEquals(expected.getStock(), prod.getStock());
		}

		String sql1 = "SELECT * FROM product_saga WHERE transaction_id = '" + transactionId + "';";
		Query query1 = entityManager.createNativeQuery(sql1, ProductSaga.class);

		List<ProductSaga> productSagas = query1.getResultList();
		Assertions.assertTrue(productSagas.size() == 0);
	}
}
