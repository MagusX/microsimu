package com.microsimu.deliveryService.mapper;
import com.microsimu.deliveryService.dto.request.DeliveryRequestDto;
import com.microsimu.deliveryService.entity.DeliveryEntity;

public final class DeliveryMapper {
	private DeliveryMapper() {}
	public static DeliveryEntity toEntityFromDto(DeliveryRequestDto dto) {
		if ( dto == null ) {
			return null;
		}

		DeliveryEntity deliveryEntity = new DeliveryEntity();

		deliveryEntity.setId( dto.getId() );
		deliveryEntity.setTransactionId( dto.getTransactionId() );
		deliveryEntity.setOrderId( dto.getOrderId() );
		deliveryEntity.setDistance( dto.getDistance() );
		deliveryEntity.setStatus( dto.getStatus() );

		return deliveryEntity;
	}
}
