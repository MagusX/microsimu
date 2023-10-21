package com.microsimu.internalGateway.infrastructure.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {
	// product
	@Value("${ktopic.product-request.name}")
	private String topicProductRequestName;
	@Value("${ktopic.product-request.partitions}")
	private int topicProductRequestPartitions;
	@Value("${ktopic.product-request.replication}")
	private short topicProductRequestReplication;

	@Value("${ktopic.product-reply.name}")
	private String topicProductReplyName;
	@Value("${ktopic.product-reply.partitions}")
	private int topicProductReplyPartitions;
	@Value("${ktopic.product-reply.replication}")
	private short topicProductReplyReplication;

	@Bean
	public NewTopic productRequestTopic() {
		return new NewTopic(topicProductRequestName, topicProductRequestPartitions, topicProductRequestReplication);
	}

	@Bean
	public NewTopic productReplyTopic() {
		return new NewTopic(topicProductReplyName, topicProductReplyPartitions, topicProductReplyReplication);
	}

	// cart-order
	@Value("${ktopic.cartorder-request.name}")
	private String topicCartOrderRequestName;
	@Value("${ktopic.cartorder-request.partitions}")
	private int topicCartOrderRequestPartitions;
	@Value("${ktopic.cartorder-request.replication}")
	private short topicCartOrderRequestReplication;

	@Value("${ktopic.cartorder-reply.name}")
	private String topicCartOrderReplyName;
	@Value("${ktopic.cartorder-reply.partitions}")
	private int topicCartOrderReplyPartitions;
	@Value("${ktopic.cartorder-reply.replication}")
	private short topicCartOrderReplyReplication;

	@Bean
	public NewTopic cartOrderRequestTopic() {
		return new NewTopic(topicCartOrderRequestName, topicCartOrderRequestPartitions, topicCartOrderRequestReplication);
	}

	@Bean
	public NewTopic cartOrderReplyTopic() {
		return new NewTopic(topicCartOrderReplyName, topicCartOrderReplyPartitions, topicCartOrderReplyReplication);
	}

	// payment
	@Value("${ktopic.payment-request.name}")
	private String topicPaymentRequestName;
	@Value("${ktopic.payment-request.partitions}")
	private int topicPaymentRequestPartitions;
	@Value("${ktopic.payment-request.replication}")
	private short topicPaymentRequestReplication;

	@Value("${ktopic.payment-reply.name}")
	private String topicPaymentReplyName;
	@Value("${ktopic.payment-reply.partitions}")
	private int topicPaymentReplyPartitions;
	@Value("${ktopic.payment-reply.replication}")
	private short topicPaymentReplyReplication;

	@Bean
	public NewTopic paymentRequestTopic() {
		return new NewTopic(topicPaymentRequestName, topicPaymentRequestPartitions, topicPaymentRequestReplication);
	}

	@Bean
	public NewTopic paymentReplyTopic() {
		return new NewTopic(topicPaymentReplyName, topicPaymentReplyPartitions, topicPaymentReplyReplication);
	}

	// delivery
	@Value("${ktopic.delivery-request.name}")
	private String topicDeliveryRequestName;
	@Value("${ktopic.delivery-request.partitions}")
	private int topicDeliveryRequestPartitions;
	@Value("${ktopic.delivery-request.replication}")
	private short topicDeliveryRequestReplication;

	@Value("${ktopic.delivery-reply.name}")
	private String topicDeliveryReplyName;
	@Value("${ktopic.delivery-reply.partitions}")
	private int topicDeliveryReplyPartitions;
	@Value("${ktopic.delivery-reply.replication}")
	private short topicDeliveryReplyReplication;

	@Bean
	public NewTopic deliveryRequestTopic() {
		return new NewTopic(topicDeliveryRequestName, topicDeliveryRequestPartitions, topicDeliveryRequestReplication);
	}

	@Bean
	public NewTopic deliveryReplyTopic() {
		return new NewTopic(topicDeliveryReplyName, topicDeliveryReplyPartitions, topicDeliveryReplyReplication);
	}

	// delivery tracking
	@Value("${ktopic.delivery-tracking.name}")
	private String topicDeliveryTrackingName;
	@Value("${ktopic.delivery-tracking.partitions}")
	private int topicDeliveryTrackingPartitions;
	@Value("${ktopic.delivery-tracking.replication}")
	private short topicDeliveryTrackingReplication;

	@Bean
	public NewTopic deliveryTrackingTopic() {
		return new NewTopic(topicDeliveryTrackingName, topicDeliveryTrackingPartitions, topicDeliveryTrackingReplication);
	}

	// delivery tracking monitor
	@Value("${ktopic.delivery-tracking-monitor.name}")
	private String topicDeliveryTrackingMonitorName;
	@Value("${ktopic.delivery-tracking-monitor.partitions}")
	private int topicDeliveryTrackingMonitorPartitions;
	@Value("${ktopic.delivery-tracking-monitor.replication}")
	private short topicDeliveryTrackingMonitorReplication;

	@Bean
	public NewTopic deliveryTrackingMonitorTopic() {
		return new NewTopic(topicDeliveryTrackingMonitorName, topicDeliveryTrackingMonitorPartitions, topicDeliveryTrackingMonitorReplication);
	}
}
