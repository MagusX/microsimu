package com.microsimu.cartOrderService.service.impl;

import com.microsimu.cartOrderService.common.constants.AppMessage;
import com.microsimu.cartOrderService.dto.request.CartItemsRequestDto;
import com.microsimu.cartOrderService.dto.request.CartRequestDto;
import com.microsimu.cartOrderService.entity.CartItemEntity;
import com.microsimu.cartOrderService.mapper.CartItemMapper;
import com.microsimu.cartOrderService.repository.CartItemRepository;
import com.microsimu.cartOrderService.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final CartItemRepository cartItemRepository;

	@Override
	public List<CartItemEntity> getCartItems(CartRequestDto requestDto) {
		return cartItemRepository.findAllByCustomerId(requestDto.getCustomerId());
	}

	@Override
	public void addToCart(CartItemsRequestDto requestDto) {
		Set<String> productIdSet = new HashSet<>();

		List<CartItemEntity> updatedList = new ArrayList<>();
		List<String> deleteList = new ArrayList<>();

		for (CartItemsRequestDto.CartItemDto dto : requestDto.getItems()) {
			if (productIdSet.contains(dto.getProductId())) {
				throw new RuntimeException(MessageFormat.format(AppMessage.INVALID_PARAM, "items"));
			}
			productIdSet.add(dto.getProductId());
			if (dto.getQuantity() > 0) {
				updatedList.add(CartItemMapper.toEntityFromDto(dto));
			} else if (dto.getId() != null) {
				deleteList.add(dto.getId());
			}
		}

		cartItemRepository.saveAll(updatedList);
		cartItemRepository.deleteAllById(deleteList);
	}
}
