package com.microsimu.productService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakeStockSagaDto {
	private String transactionId;
	private List<ProductDto> products = new ArrayList<>();

	@Data
	public static class ProductDto {
		private String id;
		private Integer quantity;
	}
}
