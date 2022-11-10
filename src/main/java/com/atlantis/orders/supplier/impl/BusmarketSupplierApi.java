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
import com.atlantis.orders.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    public Map<String, String> getShippedOrders() {
        Map<String, String> resultMap = new HashMap<>();
        List<BusmarketDocument> documentList = getShippedDocumentsList();
        for (BusmarketDocument busmarketDocument : documentList) {
            BusmarketDocument document = getDocument(busmarketDocument.getDocumentuuid());
            if (document.getCarrier().getName().equals("Нова пошта")) {
                resultMap.put(document.getCarriernumber(), document.getReceiver().getFullname());
            }
        }
        return resultMap;
    }

    @Override
    public String addCustomerOrder(Order order, boolean isDropship) {

        String orderName = "На склад " + order.getCustomer().getSurname();

        if (isDropship) {
            StringBuilder builder = new StringBuilder("Заказ ");
            builder.append(order.getCustomerOrderId()).append(" ");
            builder.append(order.getCustomer().getSurname()).append(" ");
            builder.append(order.getCustomer().getName()).append(" ");
            builder.append(order.getCustomer().getMiddleName());
            orderName = builder.substring(0, 28);
        }
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

    private List<BusmarketDocument> getShippedDocumentsList() {
        String url = "https://api.bm.parts/documents/list?type=00003A4D&period=today";
        Map<String, Object> response = getGetRequest(url, "");
        String responseJson = JsonUtils.convertObjectToJson(response.get("documents"));
        responseJson = responseJson.replace("_", "");
//        responseJson = responseJson.replace("document_uuid", "documentuuid");
//        responseJson = responseJson.replace("carrier_number", "carriernumber");
        return JsonUtils.parseJson(responseJson, new TypeReference<>() {
        });
    }

    private BusmarketDocument getDocument(String uuid) {
        String url = "https://api.bm.parts/documents/00003A4D/" + uuid;
        Map<String, Object> response = getGetRequest(url, "");
        String responseJson = JsonUtils.convertObjectToJson(response.get("document"));
        responseJson = responseJson.replace("_", "");
//        responseJson = responseJson.replace("full_name", "fullname");
//        responseJson = responseJson.replace("carrier_number", "carriernumber");
        return JsonUtils.parseJson(responseJson, new TypeReference<>() {
        });
    }

    private String getProductId(String productCode, String productBrand) {
        String url = "https://api.bm.parts/search/products?q="
                + productCode + "&brands="
                + productBrand + "&search_mode=strict";
        Map<String, Object> response = getGetRequest(url, "");
        String responseJson = JsonUtils.convertObjectToJson(response.get("products"));
        NavigableMap<String, BusmarketProduct> products = JsonUtils.parseJson(responseJson, new TypeReference<>() {
        });
        if (products.size() == 0) return "";
        return products.entrySet().stream()
                .filter(b -> (b.getValue().getBrand().equals(productBrand))
                        && (StringUtils.removeSymbols(b.getValue().getArticle()).equals(productCode)))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class BusmarketProduct {
        private String brand;
        private String article;

        public BusmarketProduct() {
        }

        public String getBrand() {
            return brand;
        }

        public String getArticle() {
            return article;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class BusmarketDocumentCarrier {
        private String uuid;
        private String name;

        public BusmarketDocumentCarrier() {
        }

        public String getUuid() {
            return uuid;
        }

        public String getName() {
            return name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class BusmarketDocumentReceiver {
        private String uuid;
        private String fullname;

        public BusmarketDocumentReceiver() {
        }

        public String getUuid() {
            return uuid;
        }

        public String getFullname() {
            return fullname;
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class BusmarketDocument {
        private BusmarketDocumentCarrier carrier;
        private BusmarketDocumentReceiver receiver;
        private String carriernumber;
        private String documentuuid;


        public BusmarketDocument() {
        }

        public BusmarketDocumentCarrier getCarrier() {
            return carrier;
        }

        public BusmarketDocumentReceiver getReceiver() {
            return receiver;
        }

        public String getCarriernumber() {
            return carriernumber;
        }

        public String getDocumentuuid() {
            return documentuuid;
        }

    }
}
