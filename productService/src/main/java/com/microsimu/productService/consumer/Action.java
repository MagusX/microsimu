package com.microsimu.productService.consumer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Action {
	public static final String CREATE_PRODUCT = "CREATE_PRODUCT";
	public static final String UPDATE_PRODUCT = "UPDATE_PRODUCT";

	// saga
	public static final String TAKE_STOCK = "TAKE_STOCK";
	public static final String TAKE_STOCK_UNDO = "TAKE_STOCK_UNDO";
}
