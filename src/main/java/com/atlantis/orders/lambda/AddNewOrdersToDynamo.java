package com.atlantis.orders.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.models.Product;
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

public class AddNewOrdersToDynamo implements RequestHandler<Object, Map<String, Object>> {

    static AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.atlantis.orders");

    public AddNewOrdersToDynamo() {
        ctx.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Autowired
    private IOrdersDynamoDbService ordersService;
    @Autowired
    private OneboxApiOrdersService oneboxApiOrdersService;
    @Autowired
    private SupplierApiFactory factory;


    @Override
    public Map<String, Object> handleRequest(Object o, Context context) {
        StringBuilder builder = new StringBuilder("Orders added :\n");
        List<OneboxOrder> supplierOrderListByStatus = oneboxApiOrdersService.getOneboxSupplierOrderListByStatus(SupplierOrderStatus.NEW.getStatusCode());
        List<Order> orders = oneboxApiOrdersService.parseToDynamoDbOrders(supplierOrderListByStatus);
        for (Order order : orders) {
            ordersService.addOrder(order);
            ISupplierApi supplierApi = factory.getSupplierApi(Integer.valueOf(order.getSupplierId()));
            if (supplierApi.getCustomerId(order.getCustomer()).isEmpty())
                supplierApi.addCustomer(order.getCustomer());
            supplierApi.addCustomerOrder(order);
            builder.append(String.format("     Заказ №  %s %s%n", order.getOrderId(), order.getCustomer().getSurname()));
            List<Product> products = order.getProducts();
            for (Product product : products) {
                builder.append(String.format("           --> %s %s %s - %s шт%n", product.getProductCode(), product.getProductBrand(),
                        product.getProductName(), product.getProductQty()));
            }
            oneboxApiOrdersService.setOneboxOrderStatus(Integer.parseInt(order.getOrderId()), SupplierOrderStatus.SENT.getStatusCode());
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
