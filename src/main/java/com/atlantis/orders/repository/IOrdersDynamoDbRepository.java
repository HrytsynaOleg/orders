package com.atlantis.orders.repository;

import com.atlantis.orders.dbtables.Order;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.stereotype.Component;

@Component
@EnableScan
public interface IOrdersDynamoDbRepository {

    Order getOrderById(final Integer orderID);
    void putNewOrder(Order order);
}
