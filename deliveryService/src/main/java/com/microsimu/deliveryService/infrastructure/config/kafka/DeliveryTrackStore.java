package com.microsimu.deliveryService.infrastructure.config.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTrackStore {
	private Long accumulatedKm;
	private Long totalDistance;
	private String status;
}
