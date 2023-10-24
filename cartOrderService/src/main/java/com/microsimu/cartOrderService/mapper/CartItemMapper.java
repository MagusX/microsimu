package com.microsimu.cartOrderService.mapper;
import com.microsimu.cartOrderService.dto.request.CartItemsRequestDto;
import com.microsimu.cartOrderService.entity.CartItemEntity;

import java.util.ArrayList;
import java.util.List;

public final class CartItemMapper {
	private CartItemMapper() {}

	public static CartItemEntity toEntityFromDto(CartItemsRequestDto.CartItemDto dto) {
		if ( dto == null ) {
			return null;
		}

		CartItemEntity cartItemEntity = new CartItemEntity();

		cartItemEntity.setId( dto.getId() );
		cartItemEntity.setCustomerId( dto.getCustomerId() );
		cartItemEntity.setProductId( dto.getProductId() );
		cartItemEntity.setName( dto.getName() );
		cartItemEntity.setPrice( dto.getPrice() );
		cartItemEntity.setQuantity( dto.getQuantity() );

		return cartItemEntity;
	}

	public static List<CartItemEntity> toLEntityFromLRequestDto(List<CartItemsRequestDto.CartItemDto> dtoList) {
		if ( dtoList == null ) {
			return null;
		}

		List<CartItemEntity> list = new ArrayList<CartItemEntity>( dtoList.size() );
		for ( CartItemsRequestDto.CartItemDto cartItemDto : dtoList ) {
			list.add( toEntityFromDto( cartItemDto ) );
		}

		return list;
	}
}
