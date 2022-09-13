package com.atlantis.orders.onebox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneboxOrderProduct {
    private String id;
    private String name;
    private String count;
    private Double pricewithdiscount;
    private OneboxProduct productinfo;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Double getPricewithdiscount() {
        return pricewithdiscount;
    }

    public void setPricewithdiscount(Double pricewithdiscount) {
        this.pricewithdiscount = pricewithdiscount;
    }

    public OneboxProduct getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(OneboxProduct productinfo) {
        this.productinfo = productinfo;
    }
}
