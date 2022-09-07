package com.atlantis.orders.repository.impl;

import com.atlantis.orders.dbtables.Orders;
import com.atlantis.orders.repository.IOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class OrderRepository implements IOrdersRepository {


    private final DynamoDbTable<Orders> ordersDynamoDbTable;

    @Autowired
    public OrderRepository(DynamoDbEnhancedClient dynamoDbenhancedClient) {
        this.ordersDynamoDbTable = dynamoDbenhancedClient.table("SupplierOrders", TableSchema.fromBean(Orders.class));
    }

    @Override
    public Orders getOrderById(final Integer orderID) {

        Key key = Key.builder().partitionValue(orderID)
//                .sortValue(orderID)
                .build();
        return ordersDynamoDbTable.getItem(key);
    }

    @Override
    public void putOrder(Orders order) {
        ordersDynamoDbTable.putItem(order);
    }

}
