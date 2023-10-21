package com.microsimu.externalGateway.infrastructure.apiHandler;

import org.springframework.http.ResponseEntity;

public final class ResponseBuilder {
	private ResponseBuilder() {
	}

	private static ResponseEntity.BodyBuilder ok() {
		return ResponseEntity.ok();
	}

	private static ResponseEntity.BodyBuilder response(int code) {
		return ResponseEntity.status(code);
	}

	private static ResponseEntity.BodyBuilder response(int code, String message) {
		return ResponseEntity.status(code).header("message", message);
	}

	private static ResponseEntity.BodyBuilder ok(String message) {
		return ok().header("message", message);
	}

	public static ResponseEntity<?> buildOkResponse() {
		return ok("OK").body(null);
	}

	public static ResponseEntity<?> buildOkResponse(String message) {
		return ok(message).body(null);
	}

	public static ResponseEntity<?> buildOkResponse(Object object) {
		return ok("OK").body(object);
	}

	public static ResponseEntity<?> buildOkResponse(String message, Object object) {
		return ok(message).body(object);
	}

	public static ResponseEntity<?> buildResponse(int code, String message) {
		return response(code, message).body(null);
	}

	public static ResponseEntity<?> buildBadRequestResponse(String message) {
		return ResponseEntity.badRequest().header("message", message).body(null);
	}

	public static ResponseEntity<?> buildErrorResponse(String message) {
		return ResponseEntity.internalServerError().header("message", message).body(null);
	}
}
