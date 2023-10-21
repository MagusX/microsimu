package com.microsimu.paymentService.mapper;
import com.microsimu.paymentService.dto.request.PaymentRequestDto;
import com.microsimu.paymentService.entity.PaymentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
	PaymentEntity toEntityFromDto(PaymentRequestDto dto);
}
