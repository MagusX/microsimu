package com.microsimu.externalGateway.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
	private String orderId;
	private List<ProductDto> products = new ArrayList<>();
	private Float value;
	private String side; // IN, OUT

	@Data
	public static class ProductDto {
		private String id;
		private Integer quantity;
	}
}
