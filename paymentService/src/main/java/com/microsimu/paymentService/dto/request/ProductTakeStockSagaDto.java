package com.microsimu.paymentService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTakeStockSagaDto {
	private String transactionId;
	private List<PaymentRequestDto.ProductDto> products = new ArrayList<>();
}
