package com.microsimu.paymentService.infrastructure.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {
	// payment-product
	@Value("${ktopic.payment-product-reply.name}")
	private String topicPaymentProductReplyName;
	@Value("${ktopic.payment-product-reply.partitions}")
	private int topicPaymentProductReplyPartitions;
	@Value("${ktopic.payment-product-reply.replication}")
	private short topicPaymentProductReplyReplication;

	@Bean
	public NewTopic paymentProductReplyTopic() {
		return new NewTopic(topicPaymentProductReplyName, topicPaymentProductReplyPartitions, topicPaymentProductReplyReplication);
	}

	// payment-delivery
	@Value("${ktopic.payment-delivery-reply.name}")
	private String topicPaymentDeliveryReplyName;
	@Value("${ktopic.payment-delivery-reply.partitions}")
	private int topicPaymentDeliveryReplyPartitions;
	@Value("${ktopic.payment-delivery-reply.replication}")
	private short topicPaymentDeliveryReplyReplication;

	@Bean
	public NewTopic paymentDeliveryReplyTopic() {
		return new NewTopic(topicPaymentDeliveryReplyName, topicPaymentDeliveryReplyPartitions, topicPaymentDeliveryReplyReplication);
	}
}
