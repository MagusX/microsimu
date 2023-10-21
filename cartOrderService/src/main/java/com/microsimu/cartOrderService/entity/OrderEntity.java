package com.microsimu.cartOrderService.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.microsimu.cartOrderService.common.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.cache.annotation.CacheKey;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "cart_order", indexes = {
		@Index(name = "order__id", columnList = "id"),
		@Index(name = "order__customerid_status", columnList = "customer_id, status")
})
@EqualsAndHashCode(callSuper = true)
public class OrderEntity extends Auditable implements Serializable {
	private static final long serialVersionUID = -2424698061465746120L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "VARCHAR(255)")
	@Getter @Setter
	private String id;
	@Column(name = "customer_id", nullable = false)
	@Getter @Setter
	private String customerId;
	@Getter @Setter
	private Long distance;
	@Column(name = "total_price")
	@Getter @Setter
	private Float totalPrice;
	@Getter @Setter
	private String status;

	@Column(name = "cart_items")
	@OneToMany(mappedBy = "cartOrder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonManagedReference
	@Getter @Setter
	private List<CartItemEntity> cartItems;
}
