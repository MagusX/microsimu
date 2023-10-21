package com.microsimu.externalGateway.payment;

import com.microsimu.externalGateway.infrastructure.apiHandler.ResponseBuilder;
import com.microsimu.externalGateway.kafka.KafkaMessageService;
import com.microsimu.externalGateway.payment.dto.request.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
	private final KafkaMessageService kafkaMessageService;

	@Value("${ktopic.payment-request.name}")
	private String topicPaymentRequestName;

	@Value("${ktopic.payment-reply.name}")
	private String topicPaymentReplyName;

	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody PaymentRequestDto dto) {
		return ResponseBuilder.buildOkResponse(
				kafkaMessageService.sendBlocking(topicPaymentRequestName, topicPaymentReplyName, Action.VERIFY_PAYMENT, dto));
	}
}
