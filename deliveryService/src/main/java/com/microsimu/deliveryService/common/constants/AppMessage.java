package com.microsimu.deliveryService.common.constants;

public final class AppMessage {
	private AppMessage() {}

	public static final String INVALID_PARAM = "400 INVALID PARAM {0}";
	public static final String USER_ALREADY_EXISTS = "400 USER ALREADY EXISTS";
	public static final String INVALID_USERNAME_PASSWORD = "400 INVALID USERNAME OR PASSWORD";
	public static final String WRONG_USERNAME = "401 WRONG USERNAME";
	public static final String WRONG_PASSWORD = "401 WRONG PASSWORD";
	public static final String ALREADY_EXISTS = "400 {0} ALREADY EXISTS";
	public static final String NOT_EXISTS = "400 {0} DOES NOT EXIST";

	public static final String SERVER_RUNTIME_EXCEPTION = "500 INTERNAL SERVER ERROR: {0}";
	public static final String SERVER_INVALID_ACTION = "1500 SERVER INVALID ACTION: {0}";
}
