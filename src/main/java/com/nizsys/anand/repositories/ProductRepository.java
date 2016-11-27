package com.nizsys.anand.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductRepository {
	private final Map<String, BigDecimal> PRODUCT_PRICE_MAP;
	private final Map<String, List<String>> PRODUCT_OFFERS_MAP;
	
	public ProductRepository() {
		PRODUCT_PRICE_MAP = new HashMap<>();
		PRODUCT_PRICE_MAP.put("apple", new BigDecimal("0.60"));
		PRODUCT_PRICE_MAP.put("orange", new BigDecimal("0.25"));
		
		PRODUCT_OFFERS_MAP = new HashMap<>();
		PRODUCT_OFFERS_MAP.put("apple", new ArrayList<>(Arrays.asList("OFFER_BOGOF")));
		PRODUCT_OFFERS_MAP.put("orange", new ArrayList<>(Arrays.asList("OFFER_3FOR2")));
	}
	
	public boolean isValidProduct(String product) {
		if (StringUtils.isEmpty(product)) {
			return false;
		}
		return PRODUCT_PRICE_MAP.keySet().contains(product.toLowerCase());
	}
	
	public boolean isValidOffer(String product, String offer) {
		if (StringUtils.isEmpty(product) || StringUtils.isEmpty(offer)) {
			return false;
		}
		product = product.toLowerCase();
		offer = offer.toUpperCase();
		if (!PRODUCT_OFFERS_MAP.keySet().contains(product)) {
			return false;
		}
		List<String> offerLst = PRODUCT_OFFERS_MAP.get(product);
		return offerLst.contains(offer);
	}
	
	public BigDecimal getProductPrice(String product) {
		if (StringUtils.isEmpty(product)) {
			return null;
		}
		return PRODUCT_PRICE_MAP.get(product.toLowerCase());
	}
	
}
