package com.microsimu.deliveryService.repository;

import com.microsimu.deliveryService.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, String> {
	Optional<DeliveryEntity> findByOrderId(String orderId);
}
