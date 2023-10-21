package com.microsimu.deliveryService.infrastructure.config.kafka;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class DeliveryTrackStoreSerdes extends Serdes.WrapperSerde<DeliveryTrackStore> {
	public DeliveryTrackStoreSerdes() {
		super(new JsonSerializer<>(), new JsonDeserializer<>(DeliveryTrackStore.class));
	}

	public static Serde<DeliveryTrackStore> serdes() {
		JsonSerializer<DeliveryTrackStore> serializer = new JsonSerializer<>();
		JsonDeserializer<DeliveryTrackStore> deserializer = new JsonDeserializer<>(DeliveryTrackStore.class);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeHeaders(false);
		return Serdes.serdeFrom(serializer, deserializer);
	}
}
