package com.atlantis.orders.models;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public class Product {
    private String productCode;
    private String productBrand;
    private String productName;
    private String productPrice;
    private String productQty;

    //    public Product(String productCode, String productBrand, String productName, String productPrice, String productQty) {
//        this.productCode = productCode;
//        this.productBrand = productBrand;
//        this.productName = productName;
//        this.productPrice = productPrice;
//        this.productQty = productQty;
//    }
    @DynamoDbAttribute("ProductCode")
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @DynamoDbAttribute("ProductBrand")
    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }
    @DynamoDbAttribute("ProductName")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    @DynamoDbAttribute("ProductPrice")
    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
    @DynamoDbAttribute("ProductQty")
    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }
}
