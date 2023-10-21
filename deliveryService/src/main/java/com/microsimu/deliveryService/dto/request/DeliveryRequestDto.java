package com.microsimu.deliveryService.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequestDto {
	private String id;
	private String transactionId;
	private String orderId;
	private Long distance;
	private String status;
}
