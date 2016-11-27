package com.nizsys.anand.utils;

import java.math.BigDecimal;

public enum Product {
	APPLE("apple", new BigDecimal("0.60")), 
	ORANGE("orange", new BigDecimal("0.25"));
	
	public String productName;
	public BigDecimal productPrice;
	Product(String productName, BigDecimal productPrice) {
		this.productName = productName;
		this.productPrice = productPrice;
	}
}
