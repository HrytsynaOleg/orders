package com.atlantis.orders.supplier;

import org.springframework.stereotype.Component;

@Component
public interface ISupplierApi {
    void addCustomer();
    void getCustomer();
    void addDelivery();
    void getDelivery();
    void setCustomerOrder();
    void addCustomerOrder();
    void getCustomerOrderStatus();
    void getCustomerOrdersList();
    Integer getSupplierId();
}
