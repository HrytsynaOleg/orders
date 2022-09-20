package com.atlantis.orders.onebox;

import com.atlantis.orders.constants.OneboxApiEndpoints;
import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.models.Product;
import com.atlantis.orders.onebox.model.OneboxOrder;
import com.atlantis.orders.onebox.model.OneboxOrderProduct;
import com.atlantis.orders.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OneboxApiOrdersService {

    IOneboxApiSecurityService securityService;

    @Autowired
    public OneboxApiOrdersService(IOneboxApiSecurityService securityService) {
        this.securityService = securityService;
    }

    public List<OneboxOrder> getOneboxOrderListByStatus(Integer statusId) {

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
        String response = OneboxApiRequest.getHttpPostRequest(OneboxApiEndpoints.ONEBOX_GET_ORDERS, securityService.getToken(), body);

        return JsonUtils.parseJson(response, new TypeReference<>() {
        });

    }

    public boolean setOneboxOrderStatus(Integer orderId, Integer statusId) {


        return true;
    }

    public List<Order> parseToDynamoDbOrders(List<OneboxOrder> orderList) {
        List<Order> orders = new ArrayList<>();
        for (OneboxOrder oneboxOrder : orderList) {
            Order order = new Order();
            order.setOrderId(oneboxOrder.getId());
            order.setCustomerOrderId(oneboxOrder.getCustomfields().get("RoditelskiiprotsessID").getValue());
            order.setStatus(oneboxOrder.getStatus().getId());
            order.setSupplierId(oneboxOrder.getClient().getId());
            List<Product> productList = new ArrayList<>();
            for (OneboxOrderProduct orderproduct : oneboxOrder.getOrderproducts()) {
                Product product = new Product();
                product.setProductCode(orderproduct.getProductinfo().getArticul());
                product.setProductBrand(orderproduct.getProductinfo().getBrand().getName());
                product.setProductName(orderproduct.getName());
                product.setProductPrice(orderproduct.getPricewithdiscount());
                product.setProductQty(Integer.valueOf(orderproduct.getCount().replace(".000","")));
                productList.add(product);
            }
            order.setProducts(productList);
            orders.add(order);
        }
        return orders;
    }

}
