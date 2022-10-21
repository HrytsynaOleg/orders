package com.atlantis.orders.supplier;

import com.atlantis.orders.dbtables.Order;
import com.atlantis.orders.models.Customer;
import com.atlantis.orders.onebox.model.OneboxOrder;
import com.atlantis.orders.onebox.model.OneboxOrderClient;
import org.springframework.stereotype.Component;

@Component
public interface ISupplierApi {
    String addCustomer(Customer customer);

    String getCustomerId(Customer customer);

    void addDelivery();

    void getDelivery();

    void setCustomerOrder();

    String addCustomerOrder(Order order, boolean isDropship);

    void getCustomerOrderStatus();

    void getCustomerOrdersList();

    Integer getSupplierId();
}
