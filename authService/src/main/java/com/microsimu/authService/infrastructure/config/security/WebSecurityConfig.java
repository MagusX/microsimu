package com.microsimu.authService.infrastructure.config.security;

import com.microsimu.authService.infrastructure.config.security.filter.JwtAuthEntryPoint;
import com.microsimu.authService.infrastructure.config.security.filter.ServiceCheckFilter;
import com.microsimu.authService.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final UserDetailsService userDetailsService;
	private final ServiceRepository serviceRepository;
	private final JwtAuthEntryPoint jwtAuthEntryPoint;
	private final PasswordEncoder passwordEncoder;
	private static final String[] ALLOWED_METHODS = {
			HttpMethod.GET.name(),
			HttpMethod.POST.name(),
			HttpMethod.PUT.name(),
			HttpMethod.PATCH.name(),
			HttpMethod.DELETE.name(),
			HttpMethod.OPTIONS.name(),
			HttpMethod.HEAD.name()
	};

	private static final String[] AUTH_WHITELIST = {
			"/api/auth/register-service"
	};

	private AuthenticationManagerBuilder authenticationManagerBuilder(HttpSecurity http) {
		return http.getSharedObject(AuthenticationManagerBuilder.class);
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return authenticationManagerBuilder(http).build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().configurationSource(corsConfigurationSource()).and()
				.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.antMatchers(AUTH_WHITELIST).permitAll();
//				.anyRequest().authenticated();
		http.addFilterBefore(new ServiceCheckFilter(serviceRepository, passwordEncoder), SecurityContextHolderFilter.class);

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOriginPatterns(List.of("*"));
		configuration.setAllowedMethods(List.of(ALLOWED_METHODS));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("message", "cache-control", "content-type", "pragma"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
