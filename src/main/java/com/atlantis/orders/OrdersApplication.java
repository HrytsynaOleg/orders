package com.atlantis.orders;

import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.models.Product;
import com.atlantis.orders.onebox.OneboxApiOrdersService;
import com.atlantis.orders.onebox.OneboxApiSecurityService;
import com.atlantis.orders.onebox.model.OneboxOrder;
import com.atlantis.orders.service.IOrdersDynamoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class OrdersApplication implements CommandLineRunner {

    @Autowired
    IOrdersDynamoDbService ordersService;
    @Autowired
    OneboxApiOrdersService oneboxApiOrdersService;

    public static void main(String[] args) {
        SpringApplication.run(OrdersApplication.class, args);
    }

    @Override
    public void run(String... args) {

        List<OneboxOrder> supplierOrderListByStatus = oneboxApiOrdersService.getOneboxOrderListByStatus(117);
        List<Order> orders = oneboxApiOrdersService.parseToDynamoDbOrders(supplierOrderListByStatus);
        for (Order order : orders) {
            ordersService.addOrder(order);
        }

        System.out.println();
    }

}
