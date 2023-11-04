package com.microsimu.authService.entity;

import com.microsimu.authService.common.Auditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "service", indexes = {
		@Index(name = "service__name", columnList = "name")
})
@EqualsAndHashCode(callSuper = true)
public class ServiceEntity extends Auditable implements Serializable {
	private static final long serialVersionUID = 594266735051964439L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "VARCHAR(255)")
	@Getter
	@Setter
	private String id;
	@Getter
	@Setter
	private String name;
	@Getter
	@Setter
	private String secret;
	@Getter
	@Setter
	private String salt;
}
