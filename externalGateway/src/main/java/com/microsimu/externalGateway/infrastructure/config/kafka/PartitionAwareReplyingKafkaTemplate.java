package com.microsimu.externalGateway.infrastructure.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.GenericMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;

import java.util.Iterator;

@Slf4j
public class PartitionAwareReplyingKafkaTemplate<K, V, R> extends ReplyingKafkaTemplate<K, V, R> {

	public PartitionAwareReplyingKafkaTemplate(ProducerFactory<K, V> producerFactory, GenericMessageListenerContainer<K, R> replyContainer) {
		super(producerFactory, replyContainer);
	}

	public RequestReplyFuture<K, V, R> sendAndReceiveDefault(String replyTopic, V data) {
		return sendAndReceive(getDefaultTopic(), replyTopic, data);
	}

	public RequestReplyFuture<K, V, R> sendAndReceiveDefault(String replyTopic, K key, V data) {
		return sendAndReceive(getDefaultTopic(), replyTopic, key, data);
	}

	public RequestReplyFuture<K, V, R> sendAndReceive(String requestTopic, String replyTopic, V data) {
		ProducerRecord<K, V> record = new ProducerRecord<>(requestTopic, data);
		return doSendAndReceive(record, replyTopic);
	}

	public RequestReplyFuture<K, V, R> sendAndReceive(String requestTopic, String replyTopic, K key, V data) {
		ProducerRecord<K, V> record = new ProducerRecord<>(requestTopic, key, data);
		return doSendAndReceive(record, replyTopic);
	}

	public RequestReplyFuture<K, V, R> sendAndReceive(ProducerRecord<K, V> record, String replyTopic) {
		return doSendAndReceive(record, replyTopic);
	}

	protected RequestReplyFuture<K, V, R> doSendAndReceive(ProducerRecord<K, V> record, String replyTopic) {
//		TopicPartition replyPartition = getFirstAssignedReplyTopicPartition(replyTopic);
		record.headers()
			.add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()))
			.add(new RecordHeader(KafkaHeaders.REPLY_PARTITION, intToBytesBigEndian(0)));
		return super.sendAndReceive(record);
	}


	private TopicPartition getFirstAssignedReplyTopicPartition(String replyTopic) {
		log.info("HERE>>> {}", getAssignedReplyTopicPartitions());
		if (getAssignedReplyTopicPartitions() != null) {
			log.info("BRO>>> {}", getAssignedReplyTopicPartitions());
			for (TopicPartition replyPartition : getAssignedReplyTopicPartitions()) {
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Using partition " + replyPartition.partition());
				}
				log.info(">>> {} {}", replyPartition.topic(), replyTopic);
				if (replyPartition.topic().startsWith(replyTopic)) {
					return replyPartition;
				}
			}
		}

		throw new KafkaException("Illegal state: No reply partition is assigned to this instance");
	}

	private static byte[] intToBytesBigEndian(final int data) {
		return new byte[] {(byte) ((data >> 24) & 0xff), (byte) ((data >> 16) & 0xff),
				(byte) ((data >> 8) & 0xff), (byte) ((data >> 0) & 0xff),};
	}
}
