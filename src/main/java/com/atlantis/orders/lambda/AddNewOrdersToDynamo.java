package com.atlantis.orders.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.onebox.OneboxApiOrdersService;
import com.atlantis.orders.onebox.model.OneboxOrder;
import com.atlantis.orders.service.IOrdersDynamoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;


public class AddNewOrdersToDynamo implements RequestHandler<Object, String> {

    static AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.atlantis.orders.service",
            "com.atlantis.orders.onebox", "com.atlantis.orders.repository", "com.atlantis.orders.config");
//    static AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
//            AddNewOrdersToDynamo.class.getPackageName(), SimpleCreator.class.getPackageName());

    public AddNewOrdersToDynamo() {
        ctx.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Autowired
//    private SimpleWriter simpleWriter;
    private IOrdersDynamoDbService ordersService;
    @Autowired
    private OneboxApiOrdersService oneboxApiOrdersService;


    @Override
    public String handleRequest(Object o, Context context) {

//        System.out.println(simpleWriter.getMessage());


        List<OneboxOrder> supplierOrderListByStatus = oneboxApiOrdersService.getOneboxSupplierOrderListByStatus(117);
        List<Order> orders = oneboxApiOrdersService.parseToDynamoDbOrders(supplierOrderListByStatus);
        for (Order order : orders) {
            ordersService.addOrder(order);
            System.out.printf("Order added - %s%n", order.getOrderId());
        }

        return "OK";
    }

}
