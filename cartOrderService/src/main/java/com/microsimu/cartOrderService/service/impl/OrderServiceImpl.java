package com.microsimu.cartOrderService.service.impl;

import com.microsimu.cartOrderService.common.OrderStatusConstant;
import com.microsimu.cartOrderService.common.constants.AppMessage;
import com.microsimu.cartOrderService.dto.request.CreateOrderRequestDto;
import com.microsimu.cartOrderService.dto.request.OrderRequestDto;
import com.microsimu.cartOrderService.entity.CartItemEntity;
import com.microsimu.cartOrderService.entity.OrderEntity;
import com.microsimu.cartOrderService.mapper.CartItemMapper;
import com.microsimu.cartOrderService.repository.CartItemRepository;
import com.microsimu.cartOrderService.repository.OrderRepository;
import com.microsimu.cartOrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final CartItemMapper cartItemMapper;
	private final OrderRepository orderRepository;
	private final CartItemRepository cartItemRepository;

	// TODO: get list orders (search by id)

	@Cacheable(value = "order", key = "#requestDto.id", unless = "#result == null")
	@Override
	public OrderEntity getOrder(OrderRequestDto requestDto) {
		return orderRepository.findById(requestDto.getId()).orElseThrow(
				() -> new RuntimeException(MessageFormat.format(AppMessage.NOT_EXISTS, "Order id: " + requestDto.getId()))
		);
	}

	@Transactional
	@Override
	public OrderEntity createOrder(CreateOrderRequestDto requestDto) {
		if (requestDto.getCartItemIds().size() == 0) return null;

		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setCustomerId(requestDto.getCustomerId());
		orderEntity.setDistance(requestDto.getDistance());
		orderEntity.setStatus(OrderStatusConstant.PENDING);

		List<CartItemEntity> cartItemEntityList = cartItemRepository.findAllById(requestDto.getCartItemIds());

		float totalPrice = 0f;
		for (CartItemEntity cartItemEntity : cartItemEntityList) {
			cartItemEntity.setCartOrder(orderEntity);

			totalPrice += cartItemEntity.getPrice() * cartItemEntity.getQuantity();
		}

		orderEntity.setTotalPrice(totalPrice);
		orderEntity.setCartItems(cartItemEntityList);

		return orderRepository.save(orderEntity);
	}

	@Override
	public OrderEntity cancelOrder(OrderRequestDto requestDto) {
		OrderEntity orderEntity = getOrder(requestDto);
		orderEntity.setStatus(OrderStatusConstant.CANCELED);

		return orderRepository.save(orderEntity);
	}
}
