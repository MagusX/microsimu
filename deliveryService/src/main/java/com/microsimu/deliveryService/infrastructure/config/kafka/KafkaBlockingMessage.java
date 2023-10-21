package com.microsimu.deliveryService.infrastructure.config.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaBlockingMessage {
	private String requestId;
	private String action;
	private Object payload;
	private RuntimeException error;

	public KafkaBlockingMessage(String requestId, String action, Object payload) {
		this.requestId = requestId;
		this.action = action;
		this.payload = payload;
	}

	public KafkaBlockingMessage(String requestId, String action, Exception error) {
		this.requestId = requestId;
		this.action = action;
		this.error = new RuntimeException(error.getMessage());
		log.error("ERROR: ", error);
	}
}
