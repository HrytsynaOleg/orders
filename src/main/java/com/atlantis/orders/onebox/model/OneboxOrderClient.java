package com.atlantis.orders.onebox.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OneboxOrderClient {
    private String id;
    private String company;
    private String name;
    private String namemiddle;
    private String namelast;
    private List<String> phones;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamemiddle() {
        return namemiddle;
    }

    public void setNamemiddle(String namemiddle) {
        this.namemiddle = namemiddle;
    }

    public String getNamelast() {
        return namelast;
    }

    public void setNamelast(String namelast) {
        this.namelast = namelast;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }
}
