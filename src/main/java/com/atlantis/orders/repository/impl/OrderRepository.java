package com.atlantis.orders.repository.impl;

import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.repository.IOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

@Repository
public class OrderRepository implements IOrdersRepository {

    private final DynamoDbTable<Order> ordersDynamoDbTable;

    @Autowired
    public OrderRepository(DynamoDbEnhancedClient dynamoDbenhancedClient) {
        this.ordersDynamoDbTable = dynamoDbenhancedClient.table("SupplierOrders", TableSchema.fromBean(Order.class));
    }

    @Override
    public Order getOrderById(final Integer orderID) {

        Key key = Key.builder().partitionValue(orderID)
//                .sortValue(orderID)
                .build();
        return ordersDynamoDbTable.getItem(key);
    }

    @Override
    public void putNewOrder(Order order) {
        PutItemEnhancedRequest<Order> request = PutItemEnhancedRequest.builder(Order.class)
                .item(order)
                .conditionExpression(Expression.builder().expression("attribute_not_exists(OrderId)").build())
                .build();
        try {
            ordersDynamoDbTable.putItem(request);
        } catch (ConditionalCheckFailedException ex) {
            System.err.println("Item already exist");
        }
    }

}
