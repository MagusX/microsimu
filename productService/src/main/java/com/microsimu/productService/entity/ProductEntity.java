package com.microsimu.productService.entity;

import com.microsimu.productService.common.Auditable;
import com.microsimu.productService.common.constants.AppMessage;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.text.MessageFormat;

@Entity
@Table(name = "product", indexes = {
		@Index(name = "product__id", columnList = "id")
})
public class ProductEntity extends Auditable implements Serializable {
	private static final long serialVersionUID = 4815178534274562067L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "VARCHAR(255)")
	@Getter @Setter
	private String id;

	@Getter @Setter
	private String name;
	@Getter @Setter
	private Float price;
	@Getter @Setter
	private Integer stock;
	@Getter @Setter
	private String category;

	@Version // optimistic lock
	@Getter @Setter
	private Long version;

	public void takeStock(int count) {
		if (stock - count < 0) {
			throw new RuntimeException(MessageFormat.format(AppMessage.SERVER_INVALID_ACTION, "Out of stock"));
		}
		stock -= count;
	}

	public void addStock(int count) {
		stock += count;
	}
}
