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
    @Autowired
    private DynamoDbEnhancedClient dynamoDbenhancedClient;

    public OrderRepository() {
        super();
    }

    @Override
    public Orders getOrder(final Integer orderID) {

        DynamoDbTable<Orders> orderTable = getTable();

        Key key = Key.builder().partitionValue(orderID)
//                .sortValue(orderID)
                .build();

        return orderTable.getItem(key);
    }


    private DynamoDbTable<Orders> getTable() {
        return dynamoDbenhancedClient.table("SupplierOrders", TableSchema.fromBean(Orders.class));
    }
}
