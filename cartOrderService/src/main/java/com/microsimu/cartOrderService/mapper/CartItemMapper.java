package com.microsimu.cartOrderService.mapper;
import com.microsimu.cartOrderService.dto.request.CartItemsRequestDto;
import com.microsimu.cartOrderService.entity.CartItemEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
	CartItemEntity toEntityFromDto(CartItemsRequestDto.CartItemDto dto);

	List<CartItemEntity> toLEntityFromLRequestDto(List<CartItemsRequestDto.CartItemDto> dtoList);
}
