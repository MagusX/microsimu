package com.microsimu.deliveryService.service.impl;

import com.microsimu.deliveryService.common.DeliveryStatusConstant;
import com.microsimu.deliveryService.common.constants.AppMessage;
import com.microsimu.deliveryService.dto.request.DeliveryRequestDto;
import com.microsimu.deliveryService.dto.request.RefundRequestDto;
import com.microsimu.deliveryService.dto.request.ReturnProductSagaDto;
import com.microsimu.deliveryService.entity.DeliveryEntity;
import com.microsimu.deliveryService.infrastructure.config.kafka.DeliveryTrackStore;
import com.microsimu.deliveryService.infrastructure.config.kafka.KafkaMessage;
import com.microsimu.deliveryService.mapper.DeliveryMapper;
import com.microsimu.deliveryService.repository.DeliveryRepository;
import com.microsimu.deliveryService.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
	@Value("${ktopic.rb-payment.name}")
	private String topicRbPayment;
	@Value("${ktopic.rb-product.name}")
	private String topicRbProduct;
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final DeliveryRepository deliveryRepository;

	public void createDelivery(DeliveryRequestDto dto) {
		DeliveryEntity deliveryEntity = DeliveryMapper.toEntityFromDto(dto);
		deliveryEntity.setStatus(DeliveryStatusConstant.DELIVERY_IN_PROGRESS);

		deliveryRepository.save(deliveryEntity);
	}

	@Async(value = "deliveryStatusExecutor")
	@Override
	public void proccessDeliveryStatus(String orderId, DeliveryTrackStore store) {
		// TODO: update order (order service) status
		if (DeliveryStatusConstant.DELIVERY_DAMAGED.equals(store.getStatus())) {
			DeliveryEntity deliveryEntity = getDetail(orderId);
			refund(orderId, deliveryEntity.getTransactionId());
			deliveryEntity.setStatus(store.getStatus());
			deliveryRepository.save(deliveryEntity);
		} else if (DeliveryStatusConstant.DELIVERY_RETURNED.equals(store.getStatus())) {
			DeliveryEntity deliveryEntity = getDetail(orderId);
			refund(orderId, deliveryEntity.getTransactionId());
			returnProduct(orderId, deliveryEntity.getTransactionId());
			deliveryEntity.setStatus(store.getStatus());
			deliveryRepository.save(deliveryEntity);
		} else if (store.getAccumulatedKm() >= store.getTotalDistance()) {
			DeliveryEntity deliveryEntity = getDetail(orderId);
			deliveryEntity.setStatus(DeliveryStatusConstant.DELIVERY_SUCCEEDED);
			deliveryRepository.save(deliveryEntity);
		}
	}

	private void refund(String orderId, String transactionId) {
		RefundRequestDto dto = new RefundRequestDto();
		dto.setOrderId(orderId);
		dto.setTransactionId(transactionId);

		kafkaTemplate.send(topicRbPayment, orderId, new KafkaMessage("REFUND", dto));
	}

	private void returnProduct(String orderId, String transactionId) {
		ReturnProductSagaDto dto = new ReturnProductSagaDto();
		dto.setTransactionId(transactionId);

		kafkaTemplate.send(topicRbProduct, orderId, new KafkaMessage("TAKE_STOCK_UNDO", dto));
	}

	@Cacheable(value = "delivery", key = "#orderId", unless = "#result == null")
	private DeliveryEntity getDetail(String orderId) {
		return deliveryRepository.findByOrderId(orderId).orElseThrow(
				() -> new RuntimeException(MessageFormat.format(AppMessage.NOT_EXISTS, "OrderId: " + orderId))
		);
	}
}
