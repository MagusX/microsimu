package com.microsimu.productService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
	private String id;
	private String name;
	private Float price;
	private Integer stock;
	private String category;
}
