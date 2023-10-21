package com.microsimu.productService.repository;

import com.microsimu.productService.entity.ProductSaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSagaRepository extends JpaRepository<ProductSaga, String> {
	List<ProductSaga> findAllByTransactionId(String transactionId);
}
