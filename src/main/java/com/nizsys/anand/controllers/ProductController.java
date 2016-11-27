package com.nizsys.anand.controllers;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nizsys.anand.services.ProductService;

@RestController
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("/products")
    public String getBillTotal(@QueryParam("productlist") String[] productlist) {
    	return productService.getBillTotalForProducts(productlist);
    }

}
