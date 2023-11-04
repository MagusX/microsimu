package com.microsimu.authService.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.microsimu.authService.common.constants.AppMessage;
import com.microsimu.authService.dto.request.UserCredentialsRequestDto;
import com.microsimu.authService.dto.response.LoginResponse;
import com.microsimu.authService.entity.RefreshTokenEntity;
import com.microsimu.authService.entity.UserEntity;
import com.microsimu.authService.repository.RefreshTokenRepository;
import com.microsimu.authService.repository.UserRepository;
import com.microsimu.authService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	@Value("${app.internal.secret}")
	private String internalSecret;
	@Value("${app.external.secret}")
	private String externalSecret;
	private static final int REFRESH_TOKEN_DURATION_S = 86400; // 24 hrs
	private static final int ACCESS_TOKEN_DURATION_S = 1800; // 0.5 hrs
	private static final String ISSUER = "MICROSIMU";
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final AuthenticationManager authenticationManager;

	@Override
	public LoginResponse authenticateLogin(UserCredentialsRequestDto loginDto) {
		UserEntity userEntity = userRepository.findByUsername(loginDto.getUsername());
		if (userEntity == null) {
			throw new RuntimeException(AppMessage.WRONG_USERNAME);
		}

		final String username = userEntity.getUsername();

		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, loginDto.getPassword() + userEntity.getSalt()));
		} catch (BadCredentialsException e) {
			throw new RuntimeException(AppMessage.WRONG_PASSWORD);
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String refreshToken = generateRefreshToken(username, userEntity.getRole(), userEntity.getAuthorities());
		String accessToken = generateAccessToken(username, userEntity.getRole(), userEntity.getAuthorities());

		return new LoginResponse(username, accessToken, refreshToken);
	}

	private String generateRefreshToken(String username, String role, Collection<? extends GrantedAuthority> authorities) {
		RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByUsername(username);
		if (refreshTokenEntity == null) {
			refreshTokenEntity = new RefreshTokenEntity();
			refreshTokenEntity.setUsername(username);
		}

		String token = generateJWT(username, role, authorities, REFRESH_TOKEN_DURATION_S);

		refreshTokenEntity.setToken(token);
		refreshTokenRepository.save(refreshTokenEntity);

		return token;
	}

	private String generateAccessToken(String username, String role, Collection<? extends GrantedAuthority> authorities) {
		return generateJWT(username, role, authorities, ACCESS_TOKEN_DURATION_S);
	}

	private String generateJWT(String username, String role, Collection<? extends GrantedAuthority> authorities, long expInSeconds) {
		LocalDateTime exp = LocalDateTime.now().plusSeconds(expInSeconds);
		String secret = "CUSTOMER".equals(role) ? externalSecret : internalSecret;

		return JWT.create()
				.withSubject(username)
				.withExpiresAt(exp.toInstant(ZoneId.systemDefault().getRules().getOffset(exp)))
				.withIssuer(ISSUER)
				.withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.withClaim("role", role)
				.sign(Algorithm.HMAC512(secret.getBytes()));
	}
}
