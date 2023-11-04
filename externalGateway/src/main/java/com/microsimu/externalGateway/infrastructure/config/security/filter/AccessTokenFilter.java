package com.microsimu.externalGateway.infrastructure.config.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {
	private final String[] AUTH_WHITELIST;
	private final String secret;
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		log.info("START request [{}] [{}] with parameters: [{}]", request.getMethod(), request.getRequestURI(), request.getQueryString());

		if (!isInWhiteList(request)) {
			String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
				try {
					decodeToken(bearerToken);
				} catch (Exception e) {
					setException(response, "401 " + e.getClass().getSimpleName());
				}
			} else {
				setException(response, "401 INVALID ACCESS TOKEN");
			}
		}

		filterChain.doFilter(request, response);
		log.info("END request [{}] [{}]", request.getMethod(), request.getRequestURI());
	}

	private DecodedJWT decodeToken(String bearerToken) {
		String accessToken = bearerToken.substring("Bearer ".length());
		JWTVerifier verifier = JWT
				.require(Algorithm.HMAC512(secret.getBytes()))
				.build();
		DecodedJWT decodedJWT = verifier.verify(accessToken);
		if (decodedJWT.getClaim("roles").isNull()) {
			throw new InvalidClaimException("");
		}

		return decodedJWT;
	}

	private void setException(HttpServletResponse response, String message) throws IOException {
		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put("message", message);

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		mapper.writeValue(response.getWriter(), errorDetails);
	}

	private boolean isInWhiteList(HttpServletRequest request) {
		String requestUrl = request.getRequestURI();

		for (String whitelistedURL : AUTH_WHITELIST) {
			if (requestUrl.contains(whitelistedURL)) {
				return true;
			}
		}

		return false;
	}
}
