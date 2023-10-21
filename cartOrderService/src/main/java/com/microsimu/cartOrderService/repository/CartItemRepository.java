package com.microsimu.cartOrderService.repository;

import com.microsimu.cartOrderService.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, String> {
	@Query(value = "SELECT * FROM cart_item WHERE customer_id = :customerId AND order_id IS NULL", nativeQuery = true)
	List<CartItemEntity> findAllByCustomerId(String customerId);
}
