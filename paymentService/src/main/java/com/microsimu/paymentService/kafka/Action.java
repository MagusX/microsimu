package com.microsimu.paymentService.kafka;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Action {
	public static final String VERIFY_PAYMENT = "VERIFY_PAYMENT";
	public static final String REFUND = "REFUND";
	public static final String TAKE_STOCK = "TAKE_STOCK";
	public static final String CREAT_DELIVERY = "CREATE_DELIVERY";
}
