package com.microsimu.productService;

import com.microsimu.productService.dto.request.ProductRequestDto;
import com.microsimu.productService.entity.ProductEntity;
import com.microsimu.productService.mapper.ProductMapper;
import com.microsimu.productService.repository.ProductRepository;
import com.microsimu.productService.repository.ProductSagaRepository;
import com.microsimu.productService.service.ProductService;
import com.microsimu.productService.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
public class ProductServiceTest {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductSagaRepository productSagaRepository;

	private ProductService productService;

	@BeforeEach
	void init() {
		productService = new ProductServiceImpl(productSagaRepository, productRepository);
	}

	@Test
	public void testCreateProduct() throws Exception {
		ProductRequestDto dto = new ProductRequestDto();
		dto.setName("prod1");
		dto.setPrice(50f);
		dto.setCategory("CAT");
		dto.setStock(100);

		ProductEntity actual = productService.createProduct(dto);
		Assertions.assertNotNull(actual.getId());
		Assertions.assertEquals("prod1", actual.getName());
		Assertions.assertEquals(50f, actual.getPrice());
		Assertions.assertEquals("CAT", actual.getCategory());
		Assertions.assertEquals(100, actual.getStock());
	}
}
