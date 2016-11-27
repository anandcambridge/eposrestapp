package com.nizsys.anand.repositories;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductRepository {
	private final Map<String, BigDecimal> PRODUCT_PRICE_MAP;
	
	public ProductRepository() {
		PRODUCT_PRICE_MAP = new HashMap<>();
		PRODUCT_PRICE_MAP.put("apple", new BigDecimal("0.60"));
		PRODUCT_PRICE_MAP.put("orange", new BigDecimal("0.25"));
	}
	
	public boolean isValidProduct(String product) {
		if (StringUtils.isEmpty(product)) {
			return false;
		}
		return PRODUCT_PRICE_MAP.keySet().contains(product.toLowerCase());
	}
	
	public BigDecimal getProductPrice(String product) {
		if (StringUtils.isEmpty(product)) {
			return null;
		}
		return PRODUCT_PRICE_MAP.get(product.toLowerCase());
	}
	
}
