package com.atlantis.orders.supplier.impl;

import com.amazonaws.transform.SimpleTypeUnmarshallers;
import com.atlantis.orders.httprequest.HttpExecutor;
import com.atlantis.orders.httprequest.HttpRequestRequest;
import com.atlantis.orders.httprequest.HttpResponseWrapper;
import com.atlantis.orders.onebox.model.OneboxOrder;
import com.atlantis.orders.onebox.model.OneboxOrderClient;
import com.atlantis.orders.onebox.model.OneboxOrderProduct;
import com.atlantis.orders.onebox.model.OneboxProduct;
import com.atlantis.orders.service.IAwsSecretService;
import com.atlantis.orders.supplier.ISupplierApi;
import com.atlantis.orders.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class BusmarketSupplierApi implements ISupplierApi {

    private final String token;

    @Autowired
    public BusmarketSupplierApi(IAwsSecretService secretService) {
        this.token = secretService.getSecretByKey("busmarket_token");
    }

    @Override
    public String addCustomer(OneboxOrderClient customer) {
        String url = "https://api.bm.parts/delivery/receivers";
        Map<String, String> body = new HashMap<>();
        body.put("surname", customer.getNamelast());
        body.put("name", customer.getName());
        body.put("middle_name", customer.getNamemiddle());
        body.put("phone", customer.getPhones().get(0));
        Map<String, Object> response = getPostRequest(url, body);
        if (response == null) return "";
        try {
            String responseJson = JsonUtils.convertObjectToJson(response.get("receiver"));
            Map<String, String> newCustomer = JsonUtils.parseJson(responseJson, new TypeReference<>() {
            });
            System.out.printf("Customer created. ID - %s", newCustomer.get("uuid"));
            return newCustomer.get("uuid");
        } catch (UnsupportedOperationException ex) {
            System.err.println(JsonUtils.convertObjectToJson(response.get("message")));
            return "";
        }

    }

    @Override
    public String getCustomerId(OneboxOrderClient customer) {
        String customerName = customer.getNamelast() + " " + customer.getName() + " " + customer.getNamemiddle();
        int pageCounter = 1;
        int pageSize = 100;
        while (pageSize > 0) {
            String url = "https://api.bm.parts/delivery/receivers?page=" + pageCounter + "&per_page=100";
            Map<String, Object> response = getGetRequest(url, "");
            if (response == null) return "";
            String responseJson = JsonUtils.convertObjectToJson(response.get("receivers"));
            List<Map<String, String>> customerList = JsonUtils.parseJson(responseJson, new TypeReference<>() {
            });
            String result = customerList.stream()
                    .filter(e -> e.get("full_name").equals(customerName))
                    .findFirst()
                    .map(e -> e.get("uuid"))
                    .orElse("");
            if (!result.isEmpty()) return result;
            pageSize = customerList.size();
            pageCounter++;
        }
        return "";
    }

    @Override
    public void addDelivery() {

    }

    @Override

    public void getDelivery() {

    }

    @Override
    public void setCustomerOrder() {

    }

    @Override
    public void addCustomerOrder(OneboxOrder order) {
        List<OneboxOrderProduct> productList = order.getOrderproducts();
        for (OneboxOrderProduct oneboxOrderProduct : productList) {
            String productId = getProductId(oneboxOrderProduct.getProductinfo().getArticul().replace("NRF", ""),
                    oneboxOrderProduct.getProductinfo().getBrand().getName());
            System.out.println(productId);
        }

    }

    @Override
    public void getCustomerOrderStatus() {

    }

    @Override
    public void getCustomerOrdersList() {

    }

    @Override
    public Integer getSupplierId() {
        return 1991;
    }

    private String getProductId(String productCode, String productBrand) {
        String url = "https://api.bm.parts/search/products?q="
                + productCode + "&brands="
                + productBrand + "&search_mode=strict";
        Map<String, Object> response = getGetRequest(url, "");
        String responseJson = JsonUtils.convertObjectToJson(response.get("products"));
        NavigableMap<String, Object> products = JsonUtils.parseJson(responseJson, new TypeReference<>() {
        });
        if (products.size() > 0) return products.firstEntry().getKey();
        return "";
    }

    private Map<String, Object> getGetRequest(String url, Object body) {
        HttpRequestRequest httpRequest = HttpRequestRequest.builder()
                .withUrl(url)
                .withBody(body)
                .withHeaderValue("Authorization", this.token)
                .build();
        HttpResponseWrapper<Map<String, Object>> response = HttpExecutor.executeGet(httpRequest, new TypeReference<>() {
        });
        if (response.getStatus().is2xxSuccessful())
            return response.getBody();
        else {
            System.err.printf("Http request error %s", response.getStatus().toString());
            return null;
        }
    }

    private Map<String, Object> getPostRequest(String url, Object body) {
        HttpRequestRequest httpRequest = HttpRequestRequest.builder()
                .withUrl(url)
                .withBody(body)
                .withHeaderValue("Content-Type", "application/json")
                .withHeaderValue("Authorization", this.token)
                .build();
        HttpResponseWrapper<Map<String, Object>> response = HttpExecutor.executePost(httpRequest, new TypeReference<>() {
        });
        if (response.getStatus().is2xxSuccessful())
            return response.getBody();
        else {
            System.err.printf("Http request error %s", response.getStatus().toString());
            return null;
        }
    }
}
