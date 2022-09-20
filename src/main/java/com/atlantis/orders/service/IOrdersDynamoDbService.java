package com.atlantis.orders.service;

import com.atlantis.orders.dbtables.Order;
import org.springframework.stereotype.Component;

@Component
public interface IOrdersDynamoDbService {

    Order getOrderById(Integer id);
    void addOrder (Order order);
}
