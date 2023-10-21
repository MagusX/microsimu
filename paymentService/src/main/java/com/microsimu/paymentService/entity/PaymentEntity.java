package com.microsimu.paymentService.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity implements Serializable {
	private static final long serialVersionUID = 6778498114354235316L;

	@Id
	private String id;
	private String transactionId;
	private String orderId;
	private String side; // IN, OUT
	private Float value;
	private LocalDateTime timestamp;
	private Boolean refunded;
	private LocalDateTime refundedAt;
}
