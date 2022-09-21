package com.atlantis.orders.supplier;

import com.atlantis.orders.onebox.model.OneboxOrder;
import com.atlantis.orders.onebox.model.OneboxOrderClient;
import org.springframework.stereotype.Component;

@Component
public interface ISupplierApi {
    String addCustomer(OneboxOrderClient customer);

    String getCustomerId(OneboxOrderClient customer);

    void addDelivery();

    void getDelivery();

    void setCustomerOrder();

    void addCustomerOrder(OneboxOrder order);

    void getCustomerOrderStatus();

    void getCustomerOrdersList();

    Integer getSupplierId();
}
