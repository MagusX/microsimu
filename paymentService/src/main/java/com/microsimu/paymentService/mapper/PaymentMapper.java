package com.microsimu.paymentService.mapper;
import com.microsimu.paymentService.dto.request.PaymentRequestDto;
import com.microsimu.paymentService.entity.PaymentEntity;

public final class PaymentMapper {
	private PaymentMapper() {}

	public static PaymentEntity toEntityFromDto(PaymentRequestDto dto) {
		if ( dto == null ) {
			return null;
		}

		PaymentEntity paymentEntity = new PaymentEntity();

		paymentEntity.setOrderId( dto.getOrderId() );
		paymentEntity.setSide( dto.getSide() );
		paymentEntity.setValue( dto.getValue() );

		return paymentEntity;
	}}
