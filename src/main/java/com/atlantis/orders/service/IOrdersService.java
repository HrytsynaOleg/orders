package com.atlantis.orders.service;

import com.atlantis.orders.dbtables.Orders;
import org.springframework.stereotype.Component;

@Component
public interface IOrdersService {

    Orders getOrderById(Integer id);
    void addOrder (Orders order);
}
