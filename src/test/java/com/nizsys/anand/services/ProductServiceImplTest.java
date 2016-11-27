package com.nizsys.anand.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.nizsys.anand.repositories.ProductRepository;
import com.nizsys.anand.utils.Product;

public class ProductServiceImplTest {
	ProductServiceImpl productServiceImpl;
	
	@Before
	public void setup() {
		productServiceImpl = new ProductServiceImpl();
	}
	
	@Test
	public void increaseProductQty_HappyPath() {
		Map<String, Integer> productQtyMap = new HashMap<>();
		productServiceImpl.increaseProductQty(productQtyMap, Product.APPLE.productName);
		int result = productQtyMap.get(Product.APPLE.productName);
		int expectedResult = 1;
		Assert.assertEquals(expectedResult, result);
		Assert.assertEquals(1, productQtyMap.size());
	}
	
	@Test
	public void increaseProductQty_ExistingProducts_HappyPath() {
		Map<String, Integer> productQtyMap = new HashMap<>();
		productQtyMap.put(Product.APPLE.productName, 4);
		productQtyMap.put(Product.ORANGE.productName, 4);
		productServiceImpl.increaseProductQty(productQtyMap, Product.APPLE.productName);
		Assert.assertEquals(5, productQtyMap.get(Product.APPLE.productName).intValue());
		Assert.assertEquals(4, productQtyMap.get(Product.ORANGE.productName).intValue());
	}
	
	@Test
	public void increaseProductQty_EmptyProduct() {
		Map<String, Integer> productQtyMap = new HashMap<>();
		productServiceImpl.increaseProductQty(productQtyMap, StringUtils.EMPTY);
		Assert.assertEquals(0, productQtyMap.size());
	}
	
	@Test
	public void increaseProductQty_NullProductQtyMap() {
		Map<String, Integer> productQtyMap = null;
		productServiceImpl.increaseProductQty(productQtyMap, Product.APPLE.productName);
		Assert.assertNull(productQtyMap);
	}
	
	@Test
	public void calculateBillTotal_HappyPath() {
		Map<String, Integer> productQtyMap = new HashMap<>();
		productQtyMap.put(Product.APPLE.productName, 1);
		List<String> errorMsgLSt = new ArrayList<>();
		ProductRepository productRepository = Mockito.mock(ProductRepository.class);
		productServiceImpl.setProductRepository(productRepository);
		Mockito.when(productRepository.getProductPrice(Product.APPLE.productName)).thenReturn(Product.APPLE.productPrice);

		BigDecimal result = productServiceImpl.calculateBillTotal(productQtyMap, errorMsgLSt);
		BigDecimal expectedresult = Product.APPLE.productPrice;
		Assert.assertEquals(expectedresult, result);
		Mockito.verify(productRepository, Mockito.times(1)).getProductPrice(Product.APPLE.productName);
	}
	
	@Test
	public void calculateBillTotal_MultiProducts_HappyPath() {
		Map<String, Integer> productQtyMap = new HashMap<>();
		productQtyMap.put(Product.APPLE.productName, 4);
		productQtyMap.put(Product.ORANGE.productName, 6);
		List<String> errorMsgLSt = new ArrayList<>();
		ProductRepository productRepository = Mockito.mock(ProductRepository.class);
		productServiceImpl.setProductRepository(productRepository);
		Mockito.when(productRepository.getProductPrice(Product.APPLE.productName)).thenReturn(Product.APPLE.productPrice);
		Mockito.when(productRepository.getProductPrice(Product.ORANGE.productName)).thenReturn(Product.ORANGE.productPrice);

		BigDecimal result = productServiceImpl.calculateBillTotal(productQtyMap, errorMsgLSt);
		BigDecimal expectedresult = Product.APPLE.productPrice.multiply(new BigDecimal("4")).add(
				Product.ORANGE.productPrice.multiply(new BigDecimal("6")))  ;
		Assert.assertEquals(expectedresult, result);
		Mockito.verify(productRepository, Mockito.times(1)).getProductPrice(Product.APPLE.productName);
		Mockito.verify(productRepository, Mockito.times(1)).getProductPrice(Product.ORANGE.productName);
	}
	
	@Test
	public void calculateBillTotal_CheckProductPriceNotFound() {
		Map<String, Integer> productQtyMap = new HashMap<>();
		productQtyMap.put("jam777", 1);
		List<String> errorMsgLSt = new ArrayList<>();
		ProductRepository productRepository = Mockito.mock(ProductRepository.class);
		productServiceImpl.setProductRepository(productRepository);
		Mockito.when(productRepository.getProductPrice("jam777")).thenReturn(null);

		BigDecimal result = productServiceImpl.calculateBillTotal(productQtyMap, errorMsgLSt);
		BigDecimal expectedresult = new BigDecimal("0.00");
		Assert.assertEquals(expectedresult, result);
		Mockito.verify(productRepository, Mockito.times(1)).getProductPrice("jam777");
		Assert.assertEquals(1, errorMsgLSt.size());
		Assert.assertEquals(errorMsgLSt.get(0), "[ERR-101] Invalid price for the product jam777.");
	}
	
	@Test
	public void calculateBillTotal_EmptyproductQtymap() {
		Map<String, Integer> productQtyMap = new HashMap<>();
		List<String> errorMsgLSt = new ArrayList<>();
		ProductRepository productRepository = Mockito.mock(ProductRepository.class);
		productServiceImpl.setProductRepository(productRepository);

		BigDecimal result = productServiceImpl.calculateBillTotal(productQtyMap, errorMsgLSt);
		BigDecimal expectedresult = new BigDecimal("0.00");
		Assert.assertEquals(expectedresult, result);
		Assert.assertEquals(0, errorMsgLSt.size());
	}
	
	@Test
	public void calculateBillTotal_NullproductQtymap() {
		Map<String, Integer> productQtyMap = null;
		List<String> errorMsgLSt = new ArrayList<>();
		ProductRepository productRepository = Mockito.mock(ProductRepository.class);
		productServiceImpl.setProductRepository(productRepository);

		BigDecimal result = productServiceImpl.calculateBillTotal(productQtyMap, errorMsgLSt);
		BigDecimal expectedresult = new BigDecimal("0.00");
		Assert.assertEquals(expectedresult, result);
		Assert.assertEquals(0, errorMsgLSt.size());
	}
	
	@Test
	public void getFormattedBillTotal_HappyPath() {
		BigDecimal billtotal = new BigDecimal("12345.6787");
		String formattedBillTotalResult = productServiceImpl.getFormattedBillTotal(billtotal);
		String expectedResult = "£12,345.68";
		Assert.assertEquals(expectedResult, formattedBillTotalResult);
	}
	
	@Test
	public void getFormattedBillTotal_NullInput() {
		BigDecimal billtotal = null;
		String formattedBillTotalResult = productServiceImpl.getFormattedBillTotal(billtotal);
		Assert.assertNull(formattedBillTotalResult);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getBillTotalForProducts_HappyPath() {
		ProductServiceImpl productServiceImplSpy = Mockito.spy(productServiceImpl);
		String[] productLst = {Product.APPLE.productName, Product.ORANGE.productName};
		ProductRepository productRepository = Mockito.mock(ProductRepository.class);
		productServiceImplSpy.setProductRepository(productRepository);
		Mockito.when(productRepository.isValidProduct(Product.APPLE.productName)).thenReturn(true);
		Mockito.when(productRepository.isValidProduct(Product.ORANGE.productName)).thenReturn(true);
		Mockito.doNothing().when(productServiceImplSpy).increaseProductQty(Mockito.anyMap(), Mockito.anyString());
		BigDecimal billtotal = new BigDecimal("0.00").add(Product.APPLE.productPrice).add(Product.ORANGE.productPrice);
		Mockito.doReturn(billtotal).
		when(productServiceImplSpy).calculateBillTotal(Mockito.anyMap(), Mockito.anyList());
		Mockito.doReturn("£0.85").
		when(productServiceImplSpy).getFormattedBillTotal(billtotal);
		
		String result = productServiceImplSpy.getBillTotalForProducts(productLst);
		Assert.assertEquals("£0.85", result);
		Mockito.verify(productRepository, Mockito.times(2)).isValidProduct(Mockito.anyString());
		Mockito.verify(productServiceImplSpy, Mockito.times(2)).increaseProductQty(Mockito.anyMap(), Mockito.anyString());
		Mockito.verify(productServiceImplSpy, Mockito.times(1)).calculateBillTotal(Mockito.anyMap(), Mockito.anyList());
		Mockito.verify(productServiceImplSpy, Mockito.times(1)).getFormattedBillTotal(Mockito.any(BigDecimal.class));
	}
	
	@Test
	public void getBillTotalForProducts_InvalidProduct() {
		ProductServiceImpl productServiceImplSpy = Mockito.spy(productServiceImpl);
		String[] productLst = {"jam777"};
		ProductRepository productRepository = Mockito.mock(ProductRepository.class);
		productServiceImplSpy.setProductRepository(productRepository);
		Mockito.when(productRepository.isValidProduct("jam777")).thenReturn(false);
		
		String result = productServiceImplSpy.getBillTotalForProducts(productLst);
		Assert.assertEquals("[ERR-100] Product 'jam777' entered is invalid.", result);
	}

	
	@SuppressWarnings("unchecked")
	@Test
	public void getBillTotalForProducts_InvalidPrice() {
		ProductServiceImpl productServiceImplSpy = Mockito.spy(productServiceImpl);
		String[] productLst = {Product.APPLE.productName};
		ProductRepository productRepository = Mockito.mock(ProductRepository.class);
		productServiceImplSpy.setProductRepository(productRepository);
		Mockito.when(productRepository.isValidProduct(Product.APPLE.productName)).thenReturn(true);
		Mockito.doNothing().when(productServiceImplSpy).increaseProductQty(Mockito.anyMap(), Mockito.anyString());
		List<String> errorMsgLst = new ArrayList<>();
		Mockito.doAnswer(new Answer<List<String>>() {

			@Override
			public List<String> answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				if (args[1] instanceof List<?>) {
					List<String> lst = (List<String>) args[1];
					lst.add(String.format("[ERR-101] Invalid price for the product %s.", Product.APPLE.productName));
				}
				return null;
			}
		}).when(productServiceImplSpy).calculateBillTotal(Mockito.anyMap(), Mockito.eq(errorMsgLst));
		
		String result = productServiceImplSpy.getBillTotalForProducts(productLst);
		String expectedResult = String.format("[ERR-101] Invalid price for the product %s.", Product.APPLE.productName);
		Assert.assertEquals(expectedResult, result);
	}

	@Test
	public void getBillTotalForProducts_EmptyProductLst() {
		ProductServiceImpl productServiceImplSpy = Mockito.spy(productServiceImpl);
		String[] productLst = new String[0];
		
		String result = productServiceImplSpy.getBillTotalForProducts(productLst);
		Assert.assertEquals("£0.00", result);
	}
	
	@Test
	public void getBillTotalForProducts_NullProductLst() {
		ProductServiceImpl productServiceImplSpy = Mockito.spy(productServiceImpl);
		String[] productLst = null;
		
		String result = productServiceImplSpy.getBillTotalForProducts(productLst);
		Assert.assertEquals("£0.00", result);
	}
	
	
}
