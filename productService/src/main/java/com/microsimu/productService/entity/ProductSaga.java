package com.microsimu.productService.entity;

import com.microsimu.productService.common.Auditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "product_saga", indexes = {
		@Index(name = "product_saga__transactionid", columnList = "transaction_id")
})
@EqualsAndHashCode(callSuper = true)
public class ProductSaga extends Auditable implements Serializable {
	private static final long serialVersionUID = 1683978064563752515L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "VARCHAR(255)")
	@Getter @Setter
	private String id;

	@Column(name = "transaction_id")
	@Getter @Setter
	private String transactionId;
	@Column(name = "product_id")
	@Getter @Setter
	private String productId;
	@Getter @Setter
	private Integer quantity;
}
