package com.nizsys.anand.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Product {
	APPLE("apple", new BigDecimal("0.60"), new ArrayList<String>(Arrays.asList("OFFER_BOGOF"))), 
	ORANGE("orange", new BigDecimal("0.25"), new ArrayList<String>(Arrays.asList("OFFER_3FOR2")));
	
	public String productName;
	public BigDecimal productPrice;
	public List<String> offerLst;
	Product(String productName, BigDecimal productPrice, List<String> offerLst) {
		this.productName = productName;
		this.productPrice = productPrice;
		this.offerLst = offerLst;
	}
}
