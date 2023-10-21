package com.microsimu.internalGateway.kafka;

import com.microsimu.internalGateway.infrastructure.config.kafka.KafkaMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class KafkaMessageService {
	private final ReplyingKafkaTemplate<String, Object, Object> replyKafkaTemplate;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public Object sendBlocking(String requestTopic, String replyTopic, String action, Object data) {
		ProducerRecord<String, Object> record = new ProducerRecord<>(requestTopic, buildMessage(action, data));
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));

		RequestReplyFuture<String, Object, Object> sendAndReceive = replyKafkaTemplate.sendAndReceive(record);

		ConsumerRecord<String, Object> consumerRecord;
		try {
			consumerRecord = sendAndReceive.get();
		} catch (ExecutionException | InterruptedException e) {
			throw new RuntimeException("Internal Server Error");
		}

		KafkaMessage response = (KafkaMessage) consumerRecord.value();
		if (response.getError() == null) {
			return response.getPayload();
		}
		throw response.getError();
	}

	public void sendAsync(String topic, String action, String key, Object data) {
		kafkaTemplate.send(topic, key, buildMessage(action, data));
	}

	public void sendAsync(String topic, String action, Object data) {
		kafkaTemplate.send(topic, buildMessage(action, data));
	}

	private KafkaMessage buildMessage(String action, Object data) {
		KafkaMessage kafkaRequest = new KafkaMessage();
		kafkaRequest.setRequestId(UUID.randomUUID().toString());
		kafkaRequest.setAction(action);
		kafkaRequest.setPayload(data);
		kafkaRequest.setError(null);
		return kafkaRequest;
	}
}
