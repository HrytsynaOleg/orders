package com.atlantis.orders;

import com.atlantis.orders.dbtables.Orders;
import com.atlantis.orders.models.Product;
import com.atlantis.orders.service.IAwsSecretService;
import com.atlantis.orders.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class OrdersApplication implements CommandLineRunner {

    @Autowired
    IOrdersService ordersService;
    @Autowired
    IAwsSecretService secretService;

    public static void main(String[] args) {
        SpringApplication.run(OrdersApplication.class, args);
    }

    @Override
    public void run(String... args)  {

        String test = secretService.getSecretByKey("test");
        System.out.println(test);

        Orders order = ordersService.getOrderById(3455);
        System.out.println(order);

        Orders newOrder = new Orders();
        newOrder.setOrderId(5678);
        newOrder.setCustomerOrderId("56474");
        newOrder.setStatus("NEW");
        newOrder.setSupplierId("12");
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductBrand("MEYLE");
        product.setProductCode("09349894589HD");
        product.setProductName("Front upper engine mouthing");
        product.setProductQty("2");
        product.setProductPrice("457.00");
        products.add(product);
        newOrder.setProducts(products);

        ordersService.addOrder(newOrder);

        System.out.println();
    }

}
