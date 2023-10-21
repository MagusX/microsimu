package com.microsimu.externalGateway.cartOrder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsRequestDto {
	private List<CartItemDto> items = new ArrayList<>();

	@Data
	public static class CartItemDto {
		private String id;
		private String customerId;
		private String productId;
		private String name;
		private Float price;
		private Integer quantity;
	}
}
