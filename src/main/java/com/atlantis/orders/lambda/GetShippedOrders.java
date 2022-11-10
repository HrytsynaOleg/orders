package com.atlantis.orders.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.atlantis.orders.models.SupplierOrderStatus;
import com.atlantis.orders.onebox.OneboxApiOrdersService;
import com.atlantis.orders.onebox.model.OneboxOrder;
import com.atlantis.orders.service.IOrdersDynamoDbService;
import com.atlantis.orders.supplier.ISupplierApi;
import com.atlantis.orders.supplier.impl.SupplierApiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GetShippedOrders implements RequestHandler<Object, Map<String, Object>> {

    static AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.atlantis.orders");

    public GetShippedOrders() {
        ctx.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Autowired
    private IOrdersDynamoDbService ordersService;
    @Autowired
    private OneboxApiOrdersService oneboxApiOrdersService;
    @Autowired
    private SupplierApiFactory factory;

    public Map<String, Object> handleRequest(Object o, Context context) {
        StringBuilder builder = new StringBuilder("Orders shipped :\n");
        List<OneboxOrder> supplierOrdersWaitShipping = oneboxApiOrdersService.
                getOneboxSupplierOrderListByStatus(SupplierOrderStatus.WAIT_FOR_DROPSHIP.getStatusCode());
        for (OneboxOrder oneboxOrder : supplierOrdersWaitShipping) {
            ISupplierApi supplierApi = factory.getSupplierApi(Integer.valueOf(oneboxOrder.getClient().getId()));
            Map<String, String> shippedOrdersMap = supplierApi.getShippedOrders();
            String customerName = oneboxOrder.getCustomfields().get("RoditelskiiprotsessPokupatel").getValue();
            if (shippedOrdersMap.containsValue(customerName)) {
                String shippedNumber = shippedOrdersMap.entrySet().stream()
                        .filter(e -> Objects.equals(e.getValue(), customerName))
                        .findFirst()
                        .get().getKey();
                builder.append(oneboxOrder.getCustomfields().get("RoditelskiiprotsessID")).append(" ");
                builder.append(customerName).append(" ");
                builder.append(shippedNumber).append(" \n");
                oneboxApiOrdersService.setOneboxOrderShippedNumber(Integer.parseInt(oneboxOrder.getId()),shippedNumber);
                oneboxApiOrdersService.setOneboxOrderStatus(Integer.parseInt(oneboxOrder.getId()),SupplierOrderStatus.RECEIVED_DROPSHIP.getStatusCode());
            }
        }

        Map<String, Object> response = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");

        response.put("statusCode", 200);
        response.put("body", builder.toString());
        response.put("headers", headers);

        return response;
    }
}
