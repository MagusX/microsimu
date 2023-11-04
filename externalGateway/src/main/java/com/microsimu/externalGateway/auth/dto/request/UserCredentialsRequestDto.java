package com.microsimu.externalGateway.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsRequestDto {
	private String email;
	private String username;
	private String password;
	private String role;
}
