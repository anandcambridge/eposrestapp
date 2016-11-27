package com.nizsys.anand.services;

import java.math.BigDecimal;


public interface ProductService {
    String getBillTotalForProducts(String[] productLst);
    String getBillTotalForProducts(String[] productLst, String[] offerlist);
    String getFormattedBillTotal(BigDecimal billtotal);
}
