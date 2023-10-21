package com.microsimu.deliveryService.infrastructure.config.kafka;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public final class KafkaMessageSerdes extends Serdes.WrapperSerde<KafkaBlockingMessage> {
	public KafkaMessageSerdes() {
		super(new JsonSerializer<>(), new JsonDeserializer<>(KafkaBlockingMessage.class));
	}

	public static Serde<KafkaBlockingMessage> serdes() {
		JsonSerializer<KafkaBlockingMessage> serializer = new JsonSerializer<>();
		JsonDeserializer<KafkaBlockingMessage> deserializer = new JsonDeserializer<>(KafkaBlockingMessage.class);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeHeaders(false);
		return Serdes.serdeFrom(serializer, deserializer);
	}
}
