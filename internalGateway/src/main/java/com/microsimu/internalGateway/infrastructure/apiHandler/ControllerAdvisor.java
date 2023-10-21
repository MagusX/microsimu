package com.microsimu.internalGateway.infrastructure.apiHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ServerErrorException;

@RestControllerAdvice
@Slf4j
public class ControllerAdvisor {

	private ResponseEntity<?> buildException(Exception e) {
		String msg = e.getMessage();
		if (e instanceof MethodArgumentNotValidException) {
			String[] msgs = msg.split("; default message \\[");
			String lastMsg = msgs[msgs.length - 1];
			lastMsg = lastMsg.substring(0, lastMsg.length() - 3);
			return ResponseBuilder.buildBadRequestResponse(lastMsg);
		} else if (e instanceof IllegalArgumentException) {
			return ResponseBuilder.buildBadRequestResponse(msg);
		}

		if (msg != null && msg.startsWith("4")) {
			return ResponseBuilder.buildResponse(Integer.parseInt(msg.substring(0, 3)), msg);
		}

		log.error("ERROR: {}", msg, e);
		return ResponseBuilder.buildErrorResponse(msg);
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<?> handleException(Exception e) {
		return buildException(e);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleException(IllegalArgumentException e) {
		return buildException(e);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleException(RuntimeException e) {
		return buildException(e);
	}

	@ExceptionHandler(HttpServerErrorException.InternalServerError.class)
	public ResponseEntity<?> handleException(HttpServerErrorException.InternalServerError e) {
		return buildException(e);
	}

	@ExceptionHandler(ServerErrorException.class)
	public ResponseEntity<?> handleException(ServerErrorException e) {
		return buildException(e);
	}
}
