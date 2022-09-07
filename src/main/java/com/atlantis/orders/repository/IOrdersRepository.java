package com.atlantis.orders.repository;

import com.atlantis.orders.dbtables.Orders;
import org.springframework.stereotype.Component;

@Component
//@EnableScan
public interface IOrdersRepository {

    Orders getOrder(final Integer orderID);
}
