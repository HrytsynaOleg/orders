package com.atlantis.orders;

import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.models.Product;
import com.atlantis.orders.onebox.OneboxApiOrdersService;
import com.atlantis.orders.onebox.OneboxApiSecurityService;
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
    OneboxApiOrdersService oneboxApiOrdersService;
    @Autowired
    OneboxApiSecurityService oneboxApiSecurityService;

    public static void main(String[] args) {
        SpringApplication.run(OrdersApplication.class, args);
    }

    @Override
    public void run(String... args)  {

        String token = oneboxApiSecurityService.getToken();
        System.out.println(token);

        oneboxApiOrdersService.getSupplierOrderListByStatus(117);

        Order order = ordersService.getOrderById(3455);
        System.out.println(order);

        Order newOrder = new Order();
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
