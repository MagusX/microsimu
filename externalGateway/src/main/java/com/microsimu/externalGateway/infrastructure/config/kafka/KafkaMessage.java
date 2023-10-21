package com.microsimu.externalGateway.infrastructure.config.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessage {
	private String requestId;
	private String action;
	private Object payload;
	private RuntimeException error; // reply
}
