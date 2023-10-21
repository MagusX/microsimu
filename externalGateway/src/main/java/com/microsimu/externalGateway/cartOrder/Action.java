package com.microsimu.externalGateway.cartOrder;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Action {
	public static final String GET_CART = "GET_CART";
	public static final String ADD_TO_CART = "ADD_TO_CART";

	public static final String GET_ORDER = "GET_ORDER";
	public static final String CREATE_ORDER = "CREATE_ORDER";
	public static final String CANCEL_ORDER = "CANCEL_ORDER";
}
