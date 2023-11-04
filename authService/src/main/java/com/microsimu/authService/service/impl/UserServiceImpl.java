package com.microsimu.authService.service.impl;

import com.microsimu.authService.common.Utils;
import com.microsimu.authService.common.constants.AppMessage;
import com.microsimu.authService.dto.request.UserCredentialsRequestDto;
import com.microsimu.authService.entity.UserEntity;
import com.microsimu.authService.repository.UserRepository;
import com.microsimu.authService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserEntity createUser(UserCredentialsRequestDto requestDto) {
		UserEntity userModel = userRepository.findByUsername(requestDto.getUsername());
		if (userModel != null) {
			throw new RuntimeException(AppMessage.USER_ALREADY_EXISTS);
		}

		if (requestDto.getUsername().isEmpty() || requestDto.getPassword().isEmpty()) {
			throw new RuntimeException(AppMessage.INVALID_USERNAME_PASSWORD);
		}

		String salt = Utils.generateRandomString(10);
		UserEntity newUser = userRepository.save(
				new UserEntity(requestDto.getEmail(), requestDto.getUsername(), passwordEncoder.encode(requestDto.getPassword() + salt), salt, requestDto.getRole()));
		newUser.setPassword(null);

		return newUser;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userModel = userRepository.findByUsername(username);
		if (userModel == null) throw new UsernameNotFoundException("User " + username + " not found");
		return userModel;
	}

//	public UserEntity getUser(String username) {
//		return userRepository.findByUsername(username);
//	}
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		UserEntity userModel = userRepository.findByUsername(username);
//		if (userModel == null) throw new UsernameNotFoundException("User " + username + " not found");
//		return userModel;
//	}
}
