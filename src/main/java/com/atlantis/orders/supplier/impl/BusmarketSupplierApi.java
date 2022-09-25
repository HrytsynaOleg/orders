package com.atlantis.orders.supplier.impl;

import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.httprequest.HttpExecutor;
import com.atlantis.orders.httprequest.HttpRequestRequest;
import com.atlantis.orders.httprequest.HttpResponseWrapper;
import com.atlantis.orders.models.Customer;
import com.atlantis.orders.models.Product;
import com.atlantis.orders.onebox.OneboxApiOrdersService;
import com.atlantis.orders.service.IAwsSecretService;
import com.atlantis.orders.supplier.ISupplierApi;
import com.atlantis.orders.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BusmarketSupplierApi implements ISupplierApi {

    private final String token;
    private final OneboxApiOrdersService oneboxApiOrdersService;

    @Autowired
    public BusmarketSupplierApi(IAwsSecretService secretService,
                                OneboxApiOrdersService oneboxApiOrdersService) {
        this.token = secretService.getSecretByKey("busmarket_token");
        this.oneboxApiOrdersService = oneboxApiOrdersService;
    }

    @Override
    public String addCustomer(Customer customer) {
        String url = "https://api.bm.parts/delivery/receivers";
        Map<String, String> body = new HashMap<>();
        body.put("surname", customer.getSurname());
        body.put("name", customer.getName());
        body.put("middle_name", customer.getMiddleName());
        body.put("phone", customer.getPhone());
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
    public String getCustomerId(Customer customer) {
        String customerName = customer.getSurname() + " " + customer.getName() + " " + customer.getMiddleName();
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
    public String addCustomerOrder(Order order) {
        StringBuilder builder = new StringBuilder("Заказ ");
        builder.append(order.getCustomerOrderId()).append(" ");
        builder.append(order.getCustomer().getSurname()).append(" ");
        builder.append(order.getCustomer().getName()).append(" ");
        builder.append(order.getCustomer().getMiddleName());
        String orderName = builder.substring(0,28);
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> productsList = new ArrayList<>();
        for (Product product : order.getProducts()) {
            String articul = product.getProductCode();
            String brand = product.getProductBrand();
            String productId = getProductId(articul, brand);
            Map<String, Object> requestProduct = new HashMap<>();
            requestProduct.put("uuid", productId);
            requestProduct.put("qnt", product.getProductQty());
            productsList.add(requestProduct);
            System.out.printf("Product - %s %s - %s %n", articul, brand, productId);
        }
        requestBody.put("name", orderName);
        requestBody.put("products", productsList);
        String url = "https://api.bm.parts/shopping/carts";
        Map<String, Object> response = getPostRequest(url, requestBody);
        try {
            Object responseCart = response.get("carts");
            String responseJson = JsonUtils.convertObjectToJson(responseCart);
            Map<String, Object> cart = JsonUtils.parseJson(responseJson, new TypeReference<>() {
            });
            return (String) cart.get("uuid");
        } catch (UnsupportedOperationException ex) {
            Object message = response.get("message");
            System.err.println(message.toString());
            return "";
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