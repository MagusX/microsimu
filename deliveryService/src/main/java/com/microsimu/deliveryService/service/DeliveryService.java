package com.microsimu.deliveryService.service;

import com.microsimu.deliveryService.dto.request.DeliveryRequestDto;
import com.microsimu.deliveryService.infrastructure.config.kafka.DeliveryTrackStore;
import org.springframework.scheduling.annotation.Async;

public interface DeliveryService {
	void createDelivery(DeliveryRequestDto dto);

	@Async(value = "deliveryStatusExecutor")
	void proccessDeliveryStatus(String orderId, DeliveryTrackStore store);
}
