package com.atlantis.orders;

import com.atlantis.orders.dbtables.Orders;
import com.atlantis.orders.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class OrdersApplication implements CommandLineRunner {

    @Autowired
    IOrdersService ordersService;

    public static void main(String[] args) {
        SpringApplication.run(OrdersApplication.class, args);
    }

    @Override
    public void run(String... args)  {

        Orders order = ordersService.getOrderById(3455);
        System.out.println(order);

    }

}
