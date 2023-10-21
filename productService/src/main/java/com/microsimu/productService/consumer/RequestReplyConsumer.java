package com.microsimu.productService.consumer;

import com.microsimu.productService.common.constants.AppMessage;
import com.microsimu.productService.dto.request.ProductRequestDto;
import com.microsimu.productService.dto.request.TakeStockSagaDto;
import com.microsimu.productService.infrastructure.config.kafka.KafkaBlockingMessage;
import com.microsimu.productService.mapper.PojoMapper;
import com.microsimu.productService.saga.ProductServiceSaga;
import com.microsimu.productService.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Slf4j
@Component
public class RequestReplyConsumer {
	@Autowired
	private PojoMapper pojoMapper;

	@Qualifier(value = "ProductServiceImpl")
	@Autowired
	private ProductService productService;

	@Qualifier(value = "ProductServiceSaga")
	@Autowired
	private ProductServiceSaga productServiceSaga;

	@KafkaListener(groupId = "${spring.kafka.consumergroup}", topics = "${ktopic.product-request.name}")
	@SendTo
	public KafkaBlockingMessage listen(ConsumerRecord<String, KafkaBlockingMessage> record) {
		KafkaBlockingMessage request = record.value();
		Object payload = request.getPayload();

		try {
			Object responsePayload = null;
			switch (request.getAction()) {
				case Action.CREATE_PRODUCT:
					responsePayload = productService.createProduct(pojoMapper.get(payload, ProductRequestDto.class));
					break;
				case Action.UPDATE_PRODUCT:
					responsePayload = productService.updateProduct(pojoMapper.get(payload, ProductRequestDto.class));
					break;
				case Action.TAKE_STOCK:
					productServiceSaga.takeStock(pojoMapper.get(payload, TakeStockSagaDto.class));
					break;
				default:
					throw new RuntimeException(MessageFormat.format(AppMessage.SERVER_INVALID_ACTION, request.getPayload()));
			}

			return new KafkaBlockingMessage(request.getRequestId(), request.getAction(), responsePayload);
		} catch (Exception e) {
			return new KafkaBlockingMessage(request.getRequestId(), request.getAction(), e);
		}
	}

	@KafkaListener(groupId = "${spring.kafka.consumergroup}", topics = "${ktopic.rb-product.name}")
	public void listenAsync(ConsumerRecord<String, KafkaBlockingMessage> record) {
		KafkaBlockingMessage request = record.value();
		Object payload = request.getPayload();

		try {
			switch (request.getAction()) {
				case Action.TAKE_STOCK_UNDO:
					productServiceSaga.takeStockUndo(pojoMapper.get(payload, TakeStockSagaDto.class));
					break;
				default:
					throw new RuntimeException(MessageFormat.format(AppMessage.SERVER_INVALID_ACTION, request.getPayload()));
			}
		} catch (Exception e) {
			log.error("ERROR: ", e);
		}
	}
}
