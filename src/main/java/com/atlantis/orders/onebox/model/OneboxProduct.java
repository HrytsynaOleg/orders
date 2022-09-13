package com.atlantis.orders.onebox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class OneboxProduct {
    private OneboxBrand brand;
    private String id;
    private String name;
    private String articul;


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

    public String getArticul() {
        return articul;
    }

    public void setArticul(String articul) {
        this.articul = articul;
    }

        public OneboxBrand getBrand() {
        return brand;
    }

    public void setBrand(OneboxBrand brand) {
        this.brand = brand;
    }
}
