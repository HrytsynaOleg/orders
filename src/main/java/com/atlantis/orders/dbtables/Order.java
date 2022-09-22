package com.atlantis.orders.dbtables;

import com.atlantis.orders.models.Customer;
import com.atlantis.orders.models.Product;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@DynamoDbBean

public class Order {

    private String orderId;
    private String customerOrderId;
    private Customer customer;
    private List<Product> products;
    private String status;
    private String supplierId;

    public Order() {
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("OrderId")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @DynamoDbAttribute("CustomerOrderId")
    public String getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    @DynamoDbAttribute("Customer")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @DynamoDbAttribute("Products")
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @DynamoDbAttribute("Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @DynamoDbAttribute("SupplierId")
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderId=" + orderId +
                ", customerOrderId='" + customerOrderId + '\'' +
                ", products=" + products +
                ", status='" + status + '\'' +
                ", supplierId='" + supplierId + '\'' +
                '}';
    }

}
