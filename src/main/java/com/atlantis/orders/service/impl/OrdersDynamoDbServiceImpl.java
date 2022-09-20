package com.atlantis.orders.service.impl;


import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.repository.IOrdersDynamoDbRepository;
import com.atlantis.orders.service.IOrdersDynamoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersDynamoDbServiceImpl implements IOrdersDynamoDbService {
    @Autowired
    private IOrdersDynamoDbRepository ordersRepository;

    @Override
    public Order getOrderById(Integer id) {
        return ordersRepository.getOrderById(id);
    }

    @Override
    public void addOrder(Order order) {
        ordersRepository.putNewOrder(order);
    }
}
