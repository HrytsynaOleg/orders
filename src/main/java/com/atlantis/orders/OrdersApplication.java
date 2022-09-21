package com.atlantis.orders;

import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.onebox.OneboxApiOrdersService;
import com.atlantis.orders.onebox.model.OneboxOrder;
import com.atlantis.orders.service.IAwsSecretService;
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

        List<OneboxOrder> supplierOrderListByStatus = oneboxApiOrdersService.getOneboxSupplierOrderListByStatus(117);
        List<Order> orders = oneboxApiOrdersService.parseToDynamoDbOrders(supplierOrderListByStatus);
        for (Order order : orders) {
            ordersService.addOrder(order);
            OneboxOrder oneboxOrder = oneboxApiOrdersService.getOneboxCustomerOrderById(order.getCustomerOrderId());
            System.out.println(oneboxOrder.getName());
        }

        OneboxOrder oneboxOrder = oneboxApiOrdersService.getOneboxCustomerOrderById("5803");
        System.out.println(oneboxOrder.getName());

        ISupplierApi supplierApi = factory.getSupplierApi(1991);

        if (supplierApi.getCustomerId(oneboxOrder.getClient()).isEmpty())
            supplierApi.addCustomer(oneboxOrder.getClient());

        System.out.printf("Customer ID - %s%n", supplierApi.getCustomerId(oneboxOrder.getClient()));

        supplierApi.addCustomerOrder(oneboxOrder);

        System.out.println();
    }

}
