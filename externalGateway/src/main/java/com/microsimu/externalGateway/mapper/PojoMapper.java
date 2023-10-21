package com.microsimu.externalGateway.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PojoMapper {
	private final ObjectMapper mapper = new ObjectMapper();

	public <T> T get(Object payload, Class<T> targetClass) throws IOException {
		byte[] json = mapper.writeValueAsBytes(payload);
		return mapper.readValue(json, targetClass);
	}
}
