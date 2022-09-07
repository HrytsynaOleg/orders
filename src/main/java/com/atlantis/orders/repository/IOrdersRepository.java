package com.atlantis.orders.repository;

import com.atlantis.orders.dbtables.Orders;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.stereotype.Component;

@Component
@EnableScan
public interface IOrdersRepository {

    Orders getOrderById(final Integer orderID);
    void putOrder (Orders order);
}
