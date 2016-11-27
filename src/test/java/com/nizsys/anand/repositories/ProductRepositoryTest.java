package com.nizsys.anand.repositories;


import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nizsys.anand.utils.Product;

public class ProductRepositoryTest {

	ProductRepository productRepository;
	
	@Before
	public void setup() {
		productRepository = new ProductRepository();
	}
	
	@Test
	public void isValidProduct_HappyPath() {
		for(Product product: Product.values()) {
			boolean result = productRepository.isValidProduct(product.productName);
			Assert.assertTrue(result);
		}
	}
	
	@Test
	public void isValidProduct_DiferentCase_HappyPath() {
		for(Product product: Product.values()) {
			boolean result = productRepository.isValidProduct(StringUtils.lowerCase(product.productName));
			Assert.assertTrue(result);
			result = productRepository.isValidProduct(StringUtils.upperCase(product.productName));
			Assert.assertTrue(result);
			result = productRepository.isValidProduct(StringUtils.capitalize(product.productName));
			Assert.assertTrue(result);
		}
	}
	
	@Test
	public void isValidProduct_CheckWrongProduct() {
		String product = "jam777";
		boolean result = productRepository.isValidProduct(product);
		Assert.assertFalse(result);
	}
	
	@Test
	public void isValidProduct_Empty() {
		String product = StringUtils.EMPTY;
		boolean result = productRepository.isValidProduct(product);
		Assert.assertFalse(result);
	}
	
	@Test
	public void isValidProduct_Null() {
		String product = null;
		boolean result = productRepository.isValidProduct(product);
		Assert.assertFalse(result);
	}
	
	@Test
	public void getProductPrice_HappyPath() {
		for(Product product: Product.values()) {
			BigDecimal result = productRepository.getProductPrice(product.productName);
			Assert.assertEquals(0, product.productPrice.compareTo(result));
		}
	}
	
	@Test
	public void getProductPrice_CheckWrongProduct() {
		String product = "jam777";
		BigDecimal result = productRepository.getProductPrice(product);
		Assert.assertNull(result);
	}
	
	@Test
	public void getProductPrice_Empty() {
		String product = StringUtils.EMPTY;
		BigDecimal result = productRepository.getProductPrice(product);
		Assert.assertNull(result);
	}
	
	@Test
	public void getProductPrice_Null() {
		String product = null;
		BigDecimal result = productRepository.getProductPrice(product);
		Assert.assertNull(result);
	}
	
}
