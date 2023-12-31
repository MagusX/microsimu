package com.microsimu.cartOrderService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequestDto {
	private String customerId;
	private Long distance;
	private List<String> cartItemIds = new ArrayList<>();
}
