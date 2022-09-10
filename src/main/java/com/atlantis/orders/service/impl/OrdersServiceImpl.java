package com.atlantis.orders.service.impl;


import com.atlantis.orders.dbtables.Orders;
import com.atlantis.orders.repository.IOrdersRepository;
import com.atlantis.orders.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersServiceImpl implements IOrdersService {
    @Autowired
    private IOrdersRepository ordersRepository;

    @Override
    public Orders getOrderById(Integer id) {
        return ordersRepository.getOrderById(id);
    }

    @Override
    public void addOrder(Orders order) {
        ordersRepository.putNewOrder(order);
    }
}