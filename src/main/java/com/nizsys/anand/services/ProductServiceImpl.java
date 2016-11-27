package com.nizsys.anand.services;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nizsys.anand.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private final String ERROR_INVALID_PRODUCT = "[ERR-100] Product '%s' entered is invalid.";
    private final String ERROR_PRICE_NOT_FOUND = "[ERR-101] Invalid price for the product %s.";
    private final String ZERO_PRICE = "£0.00";
    Logger log = Logger.getLogger(ProductServiceImpl.class);

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

	@Override
	public String getBillTotalForProducts(String[] productLst) {
		Map<String, Integer> productQtyMap = new HashMap<>();
		//validation
		if (productLst == null || productLst.length <= 0) {
			return ZERO_PRICE;
		}
		for (String product: productLst) {
			if (!productRepository.isValidProduct(product)) {
				return String.format(ERROR_INVALID_PRODUCT, product);
			} else {
				increaseProductQty(productQtyMap, product);
			}
		}
		//logic
		List<String> errorMsgLst = new ArrayList<>();
		BigDecimal billtotal = calculateBillTotal(productQtyMap, errorMsgLst);
		if(errorMsgLst.size() > 0) {
			return errorMsgLst.get(0);
		}
		String billtotalFormatted = getFormattedBillTotal(billtotal);
		return billtotalFormatted;
	}

	String getFormattedBillTotal(BigDecimal billtotal) {
		//validate
		if (billtotal == null) {
			log.error("ProductServiceImpl.getFormattedBillTotal billtotal should not be empty.");
			return null;
		}
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
		return formatter.format(billtotal);
	}

	BigDecimal calculateBillTotal(Map<String, Integer> productQtyMap, List<String> errorMsgLSt) {
		BigDecimal result = new BigDecimal("0.00");
		//validate
		if (productQtyMap == null || productQtyMap.size() <= 0 ) {
			log.error("ProductServiceImpl.calculateBillTotal productQtyMap should not be empty.");
			return result;
		}
		//logic
		Iterator<Entry<String, Integer>> iterator = productQtyMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> productQtyMapEntry = iterator.next();
			String product = productQtyMapEntry.getKey();
			int productQty = productQtyMapEntry.getValue();
			BigDecimal productPrice = productRepository.getProductPrice(product);
			if (productPrice == null) {
				String errorMsg = String.format(ERROR_PRICE_NOT_FOUND, product); 
				log.error(errorMsg);
				errorMsgLSt.add(errorMsg);
				return result;
			}
			result = result.add(productPrice.multiply(new BigDecimal(productQty)));
		}
		return result;
	}

	void increaseProductQty(Map<String, Integer> productQtyMap, String product) {
		//validate
		if (productQtyMap == null || StringUtils.isEmpty(product)) {
			log.error("ProductServiceImpl.increaseProductQty productQtyMap and product should not be empty.");
			//add to db error
			return;
		}
		//logic	
		int newQty = productQtyMap.get(product) == null ? 1 : productQtyMap.get(product) + 1; 
		productQtyMap.put(product, newQty);
	}


}
