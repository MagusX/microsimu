package com.microsimu.deliveryService.infrastructure.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {
	// rb-payment
	@Value("${ktopic.rb-payment.name}")
	private String topicRbPaymentName;
	@Value("${ktopic.rb-payment.partitions}")
	private int topicRbPaymentPartitions;
	@Value("${ktopic.rb-payment.replication}")
	private short topicRbPaymentReplication;

	@Bean
	public NewTopic rbPaymentTopic() {
		return new NewTopic(topicRbPaymentName, topicRbPaymentPartitions, topicRbPaymentReplication);
	}

	// rb-product
	@Value("${ktopic.rb-product.name}")
	private String topicRbProductName;
	@Value("${ktopic.rb-product.partitions}")
	private int topicRbProductPartitions;
	@Value("${ktopic.rb-product.replication}")
	private short topicRbProductReplication;

	@Bean
	public NewTopic rbProductTopic() {
		return new NewTopic(topicRbProductName, topicRbProductPartitions, topicRbProductReplication);
	}
}
