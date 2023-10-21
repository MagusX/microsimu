package com.microsimu.deliveryService.entity;

import com.microsimu.deliveryService.common.Auditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "delivery", indexes = {
		@Index(name = "delivery__orderid", columnList = "order_id")
})
@EqualsAndHashCode(callSuper = true)
public class DeliveryEntity extends Auditable implements Serializable {
	private static final long serialVersionUID = -1068862336762816987L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "VARCHAR(255)")
	@Getter @Setter
	private String id;
	@Column(name = "transaction_id")
	@Getter @Setter
	private String transactionId;
	@Column(name = "order_id")
	@Getter @Setter
	private String orderId;
	@Getter @Setter
	private Long distance;
	@Getter @Setter
	private String status;
}
