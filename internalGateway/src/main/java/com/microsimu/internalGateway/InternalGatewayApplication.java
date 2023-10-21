package com.microsimu.internalGateway;

import com.microsimu.internalGateway.infrastructure.apiHandler.ControllerAdvisor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Import(ControllerAdvisor.class)
public class InternalGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternalGatewayApplication.class, args);
	}

}
