package com.microsimu.authService.infrastructure.config.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsimu.authService.common.constants.AppMessage;
import com.microsimu.authService.entity.ServiceEntity;
import com.microsimu.authService.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ServiceCheckFilter extends OncePerRequestFilter {
	private final ObjectMapper mapper = new ObjectMapper();
	private final ServiceRepository serviceRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String name = request.getHeader("name");
		String secret = request.getHeader("secret");

		ServiceEntity service = serviceRepository.findByName(name).orElse(null);

		if (service == null) {
			setException(response, MessageFormat.format(AppMessage.SERVER_INVALID_ACTION, "INVALID name when get token for service " + name));
		} else {
			if (!passwordEncoder.matches(secret + service.getSalt(), service.getSecret())) {
				setException(response, MessageFormat.format(AppMessage.SERVER_INVALID_ACTION, "INVALID secret when get token for service " + secret));
			}
		}

		filterChain.doFilter(request, response);
		log.info("END request [{}] [{}]", request.getMethod(), request.getRequestURI());
	}

	private void setException(HttpServletResponse response, String message) throws IOException {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put("message", message);

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		mapper.writeValue(response.getWriter(), errorDetails);
	}
}
