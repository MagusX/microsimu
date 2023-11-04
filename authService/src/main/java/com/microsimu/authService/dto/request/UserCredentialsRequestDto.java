package com.microsimu.authService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsRequestDto {
	private String email;
	private String username;
	private String password;
	private String role;
}
