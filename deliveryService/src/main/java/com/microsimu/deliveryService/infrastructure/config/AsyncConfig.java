package com.microsimu.deliveryService.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
	@Bean(name = "deliveryStatusExecutor")
	public ThreadPoolTaskExecutor deliveryStatusExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(1000);
		executor.setQueueCapacity(10000);
		executor.setKeepAliveSeconds(120);
		return executor;
	}
}
