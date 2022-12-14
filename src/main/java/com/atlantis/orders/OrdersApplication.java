package com.atlantis.orders;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.lambda.GetShippedOrders;
import com.atlantis.orders.models.SupplierOrderStatus;
import com.atlantis.orders.onebox.OneboxApiOrdersService;
import com.atlantis.orders.onebox.model.OneboxOrder;
import com.atlantis.orders.service.IOrdersDynamoDbService;
import com.atlantis.orders.supplier.ISupplierApi;
import com.atlantis.orders.supplier.impl.SupplierApiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class OrdersApplication implements CommandLineRunner {

    @Autowired
    IOrdersDynamoDbService ordersService;
    @Autowired
    OneboxApiOrdersService oneboxApiOrdersService;
    @Autowired
    SupplierApiFactory factory;

    public static void main(String[] args) {
        SpringApplication.run(OrdersApplication.class, args);
    }

    @Override
    public void run(String... args) {


//        List<OneboxOrder> supplierOrderListByStatus = oneboxApiOrdersService.getOneboxSupplierOrderListByStatus(117);
//        List<Order> orders = oneboxApiOrdersService.parseToDynamoDbOrders(supplierOrderListByStatus);
//        for (Order order : orders) {
//            ordersService.addOrder(order);
//            ISupplierApi supplierApi = factory.getSupplierApi(Integer.valueOf(order.getSupplierId()));
//            if (supplierApi.getCustomerId(order.getCustomer()).isEmpty())
//                supplierApi.addCustomer(order.getCustomer());
//            supplierApi.addCustomerOrder(order);
//            oneboxApiOrdersService.setOneboxOrderStatus(Integer.parseInt(order.getOrderId()), SupplierOrderStatus.SENT.getStatusCode());
//            System.out.println();
//        }



        System.out.println();
    }

}
