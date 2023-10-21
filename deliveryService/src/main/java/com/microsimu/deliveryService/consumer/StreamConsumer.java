package com.microsimu.deliveryService.consumer;

import com.microsimu.deliveryService.common.DeliveryStatusConstant;
import com.microsimu.deliveryService.dto.request.DeliveryTrackingDto;
import com.microsimu.deliveryService.infrastructure.config.kafka.*;
import com.microsimu.deliveryService.mapper.PojoMapper;
import com.microsimu.deliveryService.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StreamConsumer {
	private final PojoMapper pojoMapper = new PojoMapper();
	@Value("${ktopic.delivery-tracking.name}")
	private String topicStreamDeliveryTracking;
	@Value("${ktopic.delivery-tracking-monitor.name}")
	private String topicStreamDeliveryTrackingMonitor;

	private final DeliveryService deliveryService;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	public void buildPipeline(StreamsBuilder streamsBuilder) {
		streamsBuilder
		.stream(topicStreamDeliveryTracking, Consumed.with(Serdes.String(), KafkaMessageSerdes.serdes()))
		.groupByKey()
		.aggregate(
			() -> new DeliveryTrackStore(0L, null, DeliveryStatusConstant.DELIVERY_IN_PROGRESS),
			(key, dto, aggregate) -> {
				DeliveryTrackingDto trackingDto = new DeliveryTrackingDto();
				try {
					trackingDto = pojoMapper.get(dto.getPayload(), DeliveryTrackingDto.class);
				} catch (Exception e) {
					log.error("PojoMapper failed to get payload of " + key, e);
				}

				aggregate.setTotalDistance(trackingDto.getDistance());
				aggregate.setAccumulatedKm(aggregate.getAccumulatedKm() + trackingDto.getKm());
				aggregate.setStatus(aggregate.getAccumulatedKm() >= trackingDto.getDistance() ? DeliveryStatusConstant.DELIVERY_SUCCEEDED : trackingDto.getStatus());

				return aggregate;
			},
			Materialized.as("accumulated-km").with(Serdes.String(), DeliveryTrackStoreSerdes.serdes()))
		.toStream()
		.map((key, row) -> {
			deliveryService.proccessDeliveryStatus(key, row);
			return new KeyValue<>(key, new KafkaBlockingMessage(null, "DELIVERY_MONITOR", row));
		})
		.to(topicStreamDeliveryTrackingMonitor, Produced.with(Serdes.String(), KafkaMessageSerdes.serdes()));

	}
}
