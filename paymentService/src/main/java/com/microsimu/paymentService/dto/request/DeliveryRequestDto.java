package com.microsimu.paymentService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
