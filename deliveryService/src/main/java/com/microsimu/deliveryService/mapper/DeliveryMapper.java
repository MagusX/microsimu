package com.microsimu.deliveryService.mapper;
import com.microsimu.deliveryService.dto.request.DeliveryRequestDto;
import com.microsimu.deliveryService.entity.DeliveryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
	DeliveryEntity toEntityFromDto(DeliveryRequestDto dto);
}
