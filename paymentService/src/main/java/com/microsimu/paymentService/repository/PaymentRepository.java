package com.microsimu.paymentService.repository;

import com.microsimu.paymentService.entity.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentEntity, String> {
	Optional<PaymentEntity> findByOrderId(String orderId);
}
