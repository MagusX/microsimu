package com.microsimu.externalGateway.delivery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTrackingDto {
	private String orderId;
	private Long km;
	private Long distance;
	private String status;
}
