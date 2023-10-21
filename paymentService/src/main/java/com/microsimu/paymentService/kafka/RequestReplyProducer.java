package com.microsimu.paymentService.kafka;

import com.microsimu.paymentService.infrastructure.config.kafka.KafkaBlockingMessage;
import com.microsimu.paymentService.mapper.PojoMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class RequestReplyProducer {
	private final ReplyingKafkaTemplate<String, Object, Object> replyKafkaTemplate;
	private final PojoMapper pojoMapper;

	public Object sendBlocking(String requestTopic, String replyTopic, String action, Object data) {
		KafkaBlockingMessage kafkaRequest = new KafkaBlockingMessage();
		kafkaRequest.setRequestId(UUID.randomUUID().toString());
		kafkaRequest.setAction(action);
		kafkaRequest.setPayload(data);

		ProducerRecord<String, Object> record = new ProducerRecord<>(requestTopic, kafkaRequest);
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));

		RequestReplyFuture<String, Object, Object> sendAndReceive = replyKafkaTemplate.sendAndReceive(record);

		ConsumerRecord<String, Object> consumerRecord;
		try {
			consumerRecord = sendAndReceive.get();
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException("Internal Server Error");
		}

		KafkaBlockingMessage response = (KafkaBlockingMessage) consumerRecord.value();
		if (response.getError() == null) {
			return response.getPayload();
		}
		throw response.getError();
	}
}
