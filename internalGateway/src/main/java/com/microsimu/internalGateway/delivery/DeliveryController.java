package com.microsimu.internalGateway.delivery;

import com.microsimu.internalGateway.delivery.dto.request.DeliveryTrackingDto;
import com.microsimu.internalGateway.infrastructure.apiHandler.ResponseBuilder;
import com.microsimu.internalGateway.kafka.KafkaMessageService;
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
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {
	private final KafkaMessageService kafkaMessageService;

	@Value("${ktopic.delivery-request.name}")
	private String topicDeliveryRequestName;

	@Value("${ktopic.delivery-reply.name}")
	private String topicDeliveryReplyName;

	@Value("${ktopic.delivery-tracking.name}")
	private String topicDeliveryTrackingName;

	@PostMapping("/track")
	public ResponseEntity<?> track(@RequestBody DeliveryTrackingDto dto) {
		kafkaMessageService.sendAsync(topicDeliveryTrackingName, Action.TRACK_DELIVERY, dto.getOrderId(), dto);
		return ResponseBuilder.buildOkResponse();
	}
}
