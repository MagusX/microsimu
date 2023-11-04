package com.microsimu.authService.entity;

import com.microsimu.authService.common.Auditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "refresh_token")
@EqualsAndHashCode(callSuper = true)
public class RefreshTokenEntity extends Auditable implements Serializable {
	private static final long serialVersionUID = -8601772385916137487L;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "VARCHAR(255)")
	@Getter @Setter
	private String id;
	@Getter @Setter
	private String username;
	@Getter @Setter
	private String token;
}
