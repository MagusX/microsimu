package com.microsimu.cartOrderService.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microsimu.cartOrderService.common.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cart_item", indexes = {
		@Index(name = "cart_item__customerid_orderid", columnList = "customer_id, order_id")
})
@EqualsAndHashCode(callSuper = true)
public class CartItemEntity extends Auditable implements Serializable {
	private static final long serialVersionUID = -7491236995951494154L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "VARCHAR(255)")
	@Getter @Setter
	private String id;
	@Column(name = "customer_id", nullable = false)
	@Getter @Setter
	private String customerId;
	@Column(name = "product_id", nullable = false)
	@Getter @Setter
	private String productId;
	@Getter @Setter
	private String name;
	@Getter @Setter
	private Float price;
	@Getter @Setter
	private Integer quantity;
	@OneToOne
	@JsonBackReference
	@JoinColumn(name = "order_id")
	@Getter @Setter
	private OrderEntity cartOrder;
}
