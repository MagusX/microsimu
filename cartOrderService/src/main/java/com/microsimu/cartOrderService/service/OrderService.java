package com.microsimu.cartOrderService.service;

import com.microsimu.cartOrderService.dto.request.CreateOrderRequestDto;
import com.microsimu.cartOrderService.dto.request.OrderRequestDto;
import com.microsimu.cartOrderService.entity.OrderEntity;

public interface OrderService {
	OrderEntity getOrder(OrderRequestDto requestDto);

	OrderEntity createOrder(CreateOrderRequestDto requestDto);

	OrderEntity cancelOrder(OrderRequestDto requestDto);
}
