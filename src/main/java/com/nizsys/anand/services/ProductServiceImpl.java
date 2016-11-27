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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nizsys.anand.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	private ProductRepository productRepository;
    private final String ERROR_INVALID_PRODUCT = "[ERR-100] Product '%s' entered is invalid.";
    private final String ERROR_PRICE_NOT_FOUND = "[ERR-101] Invalid price for the product %s.";
    private static final String ERROR_OFFER_SYNTAX = "[ERR-102] Invalid offer syntax for offer %s.";
	private static final String ERROR_INVALID_OFFER = "[ERR-103] Invalid OFFER '%s' for the product %s.";;
	private static final String ERROR_INVALID_PRODUCT_IN_OFFER = "[ERR-104] Invalid product %s in OFFER '%s' .";;
    
    private static final String OFFER_SEPARATOR = "-";
    private final String ZERO_PRICE = "£0.00";
    Logger log = Logger.getLogger(ProductServiceImpl.class);

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

	@Override
	public String getBillTotalForProducts(String[] productLst) {
		return getBillTotalForProducts(productLst, null);
	}

	@Override
	public String getBillTotalForProducts(String[] productLst, String[] offerlist) {
		Map<String, Integer> productQtyMap = new HashMap<>();
		List<String> errorMsgLst = new ArrayList<>();
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
		Map<String, List<String>> productToOfferMap = null;
		if (offerlist != null) {
			productToOfferMap = new HashMap<>();
			fillProductToOfferMap(offerlist, productToOfferMap, errorMsgLst);
			if(errorMsgLst.size() > 0) {
				return errorMsgLst.get(0);
			}
		}
		//logic
		BigDecimal billtotal = calculateBillTotal(productQtyMap, errorMsgLst, productToOfferMap);
		if(errorMsgLst.size() > 0) {
			return errorMsgLst.get(0);
		}
		String billtotalFormatted = getFormattedBillTotal(billtotal);
		return billtotalFormatted;
	}

	/**
	 * for the given offerlist (userinput), fill the map productToOfferMap.
	 * each product can have list of offers but we use the first one for cal
	 * @param offerlist
	 * @param productToOfferMap
	 * @param errorMsgLSt
	 */
	void fillProductToOfferMap(String[] offerlist,
			Map<String, List<String>> productToOfferMap, List<String> errorMsgLSt) {
		//validate
		if (productToOfferMap == null) {
			log.error("ProductServiceImpl.fillProductToOfferMap productToOfferMap should not be null.");
			return;
		}
		if (offerlist == null || offerlist.length <= 0) {
			log.error("ProductServiceImpl.fillProductToOfferMap offerlist should not be empty.");
			return;
		}
		//logic
		for(String offer: offerlist) {
			String[] offerPrdArr = StringUtils.split(offer, OFFER_SEPARATOR, 2);
			if (offerPrdArr == null || offerPrdArr.length != 2) {
				String errMsg = String.format(ERROR_OFFER_SYNTAX, offer);
				errorMsgLSt.add(errMsg);
				return;
			}
			String offerName = offerPrdArr[0];
			String productName = offerPrdArr[1];
			if (!productRepository.isValidProduct(productName)) {
				String errMsg = String.format(ERROR_INVALID_PRODUCT_IN_OFFER, productName, offerName);
				errorMsgLSt.add(errMsg);
				return;
			}
			if (!productRepository.isValidOffer(productName, offerName)) {
				String errMsg = String.format(ERROR_INVALID_OFFER, offerName, productName);
				errorMsgLSt.add(errMsg);
				return;
			}
			List<String> offerLst = productToOfferMap.get(offer);
			if (offerLst == null) {
				offerLst = new ArrayList<String>();
			}
			offerLst.add(offerName);
			productToOfferMap.put(productName, offerLst);
		}
	}

	/**
	 * format the bill total before displaying to client, currently using uk locale.
	 * @param billtotal
	 * @return
	 */
	@Override
	public String getFormattedBillTotal(BigDecimal billtotal) {
		//validate
		if (billtotal == null) {
			log.error("ProductServiceImpl.getFormattedBillTotal billtotal should not be empty.");
			return null;
		}
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
		return formatter.format(billtotal);
	}

	BigDecimal calculateBillTotal(Map<String, Integer> productQtyMap, List<String> errorMsgLSt) {
		return calculateBillTotal(productQtyMap, errorMsgLSt, null);
	}
	
	/**
	 * calculates the bill total using OfferCalculator. We have product and qty(passed in parameter), now we need to 
	 * get the product price
	 * and offer for the product 
	 * then using OfferCalculator we can get the total price for each product.
	 * @param productQtyMap
	 * @param errorMsgLSt
	 * @param productToOfferMap
	 * @return
	 */
	BigDecimal calculateBillTotal(Map<String, Integer> productQtyMap, List<String> errorMsgLSt, Map<String, List<String>> productToOfferMap) {
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
			String offer = "NO_OFFER";
			if (productToOfferMap != null && productToOfferMap.get(product) != null) {
				//taking the first offer for the product (when more than 
				//one offer assigned to a product)
				offer = productToOfferMap.get(product).get(0);
			}
			offer = offer.toUpperCase();
			OfferCalculator offerCalc = OfferCalculator.valueOf(offer);
			result = result.add(offerCalc.calculatePrice(productPrice, productQty));
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
