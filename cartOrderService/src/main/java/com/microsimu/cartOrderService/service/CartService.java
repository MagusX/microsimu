package com.microsimu.cartOrderService.service;

import com.microsimu.cartOrderService.dto.request.CartItemsRequestDto;
import com.microsimu.cartOrderService.dto.request.CartRequestDto;
import com.microsimu.cartOrderService.entity.CartItemEntity;

import java.util.List;

public interface CartService {
	List<CartItemEntity> getCartItems(CartRequestDto requestDto);

	void addToCart(CartItemsRequestDto requestDto);
}
