package com.nizsys.anand.services;

import java.math.BigDecimal;

public enum OfferCalculator {
	
	NO_OFFER() {
		@Override
		public BigDecimal calculatePrice(BigDecimal productPrice, int productQty) {
			return productPrice.multiply(new BigDecimal(productQty));
		}
	},
	OFFER_BOGOF() {
		@Override
		public BigDecimal calculatePrice(BigDecimal productPrice, int productQty) {
			int calcQty = productQty/2;
			calcQty += productQty %2;
			return productPrice.multiply(new BigDecimal(String.valueOf(calcQty)));
		}
	},
	OFFER_3FOR2() {
		@Override
		public BigDecimal calculatePrice(BigDecimal productPrice, int productQty) {
			int calcQty = productQty/3;
			calcQty *= 2;
			calcQty += productQty %3;
			return productPrice.multiply(new BigDecimal(String.valueOf(calcQty)));
		}
	};
	
	public BigDecimal calculatePrice(BigDecimal productPrice, int productQty) {
		return null;
	}
}
