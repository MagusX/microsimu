package com.microsimu.paymentService.service;

import com.microsimu.paymentService.dto.request.PaymentRequestDto;
import com.microsimu.paymentService.dto.request.RefundRequestDto;
import com.microsimu.paymentService.entity.PaymentEntity;

public interface PaymentService {
	PaymentEntity verifyPayment(PaymentRequestDto requestDto);

	void refund(RefundRequestDto requestDto);
}
