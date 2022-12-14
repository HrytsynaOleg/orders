package com.atlantis.orders.onebox;

import com.atlantis.orders.constants.OneboxApiEndpoints;
import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.models.Customer;
import com.atlantis.orders.models.Product;
import com.atlantis.orders.onebox.model.OneboxOrder;
import com.atlantis.orders.onebox.model.OneboxOrderProduct;
import com.atlantis.orders.repository.IBrandSuffixDynamoDbRepository;
import com.atlantis.orders.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OneboxApiOrdersService {

    private final IBrandSuffixDynamoDbRepository brandSuffixRepository;

    @Autowired
    public OneboxApiOrdersService(IBrandSuffixDynamoDbRepository brandSuffixRepository) {

        this.brandSuffixRepository = brandSuffixRepository;
    }

    public List<OneboxOrder> getOneboxSupplierOrderListByStatus(Integer statusId) {

        Map<String, Object> body = new HashMap<>();
        List<String> orderFields = Arrays.asList("id", "cdate", "name", "client", "workflow", "status", "orderproducts", "customfields");
        List<String> productFields = Arrays.asList("id", "name", "articul", "brand");

//        List<String> orderFields = Arrays.asList("id","cdate","name");
        Map<String, Integer> filters = new HashMap<>();
        filters.put("workflowid", 16);
        filters.put("statusid", statusId);
        body.put("fields", orderFields);
        body.put("productfields", productFields);
        body.put("filter", filters);
        String response = OneboxApiRequest.getHttpPostRequest(OneboxApiEndpoints.ONEBOX_GET_ORDERS, body);

        return JsonUtils.parseJson(response, new TypeReference<>() {
        });

    }

    public OneboxOrder getOneboxCustomerOrderById(String orderId) {

        Map<String, Object> body = new HashMap<>();
        List<String> orderFields = Arrays.asList("id", "cdate", "name", "client", "workflow", "status", "orderproducts", "customfields");
        List<String> productFields = Arrays.asList("id", "name", "articul", "brand");
        Map<String, Object> filters = new HashMap<>();
        filters.put("workflowid", 15);
        filters.put("id", orderId);
        body.put("fields", orderFields);
        body.put("productfields", productFields);
        body.put("filter", filters);
        String response = OneboxApiRequest.getHttpPostRequest(OneboxApiEndpoints.ONEBOX_GET_ORDERS, body);
        List<OneboxOrder> orderList = JsonUtils.parseJson(response, new TypeReference<>() {
        });
        if (orderList.size() > 0) return orderList.get(0);
        return null;
    }

    public OneboxOrder getOneboxSupplierOrderById(String orderId) {

        Map<String, Object> body = new HashMap<>();
        List<String> orderFields = Arrays.asList("id", "cdate", "name", "client", "workflow", "status", "orderproducts", "customfields");
        List<String> productFields = Arrays.asList("id", "name", "articul", "brand");
        Map<String, Object> filters = new HashMap<>();
        filters.put("workflowid", 16);
        filters.put("id", orderId);
        body.put("fields", orderFields);
        body.put("productfields", productFields);
        body.put("filter", filters);
        String response = OneboxApiRequest.getHttpPostRequest(OneboxApiEndpoints.ONEBOX_GET_ORDERS, body);
        List<OneboxOrder> orderList = JsonUtils.parseJson(response, new TypeReference<>() {
        });
        if (orderList.size() > 0) return orderList.get(0);
        return null;
    }

    public int setOneboxOrderStatus(int orderId, int statusId) {
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderid", orderId);
        orderMap.put("statusid", statusId);
        List<Map<String, Object>> body = new ArrayList<>();
        body.add(orderMap);
        String response = OneboxApiRequest.getHttpPostRequest(OneboxApiEndpoints.ONEBOX_SET_ORDERS, body);
        List<Integer> responseMap = JsonUtils.parseJson(response, new TypeReference<>() {
        });
        int result = responseMap.get(0);
        return result;
    }

    public int setOneboxOrderShippedNumber(int orderId, String number) {
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderid", orderId);
        orderMap.put("deliverynote", number);
        List<Map<String, Object>> body = new ArrayList<>();
        body.add(orderMap);
        String response = OneboxApiRequest.getHttpPostRequest(OneboxApiEndpoints.ONEBOX_SET_ORDERS, body);
        List<Integer> responseMap = JsonUtils.parseJson(response, new TypeReference<>() {
        });
        int result = responseMap.get(0);
        return result;
    }

    public List<Order> parseToDynamoDbOrders(List<OneboxOrder> orderList) {
        List<Order> orders = new ArrayList<>();
        for (OneboxOrder oneboxOrder : orderList) {
            Order order = new Order();
            order.setOrderId(oneboxOrder.getId());
            String customerOrderId = oneboxOrder.getCustomfields().get("RoditelskiiprotsessID").getValue();
            order.setCustomerOrderId(customerOrderId);
            order.setStatus(oneboxOrder.getStatus().getId());
            order.setSupplierId(oneboxOrder.getClient().getId());
            List<Product> productList = new ArrayList<>();
            for (OneboxOrderProduct orderproduct : oneboxOrder.getOrderproducts()) {
                Product product = new Product();
                String articul = orderproduct.getProductinfo().getArticul();
                String brand = orderproduct.getProductinfo().getBrand().getName();
                if (!brandSuffixRepository.getSuffix(brand).isEmpty())
                    articul = articul.substring(0, articul.length() - 3);
                product.setProductCode(articul);
                product.setProductBrand(brand);
                product.setProductName(orderproduct.getName());
                product.setProductPrice(orderproduct.getPricewithdiscount());
                product.setProductQty(Integer.valueOf(orderproduct.getCount().replace(".000", "")));
                productList.add(product);
            }
            order.setProducts(productList);
            OneboxOrder customerOrder = getOneboxCustomerOrderById(customerOrderId);
            Customer customer = new Customer();
            customer.setId(customerOrder.getClient().getId());
            customer.setName(customerOrder.getClient().getName());
            customer.setSurname(customerOrder.getClient().getNamelast());
            customer.setMiddleName(customerOrder.getClient().getNamemiddle());
            customer.setPhone(customerOrder.getClient().getPhones().get(0));
            order.setCustomer(customer);
            orders.add(order);
        }
        return orders;
    }

}
