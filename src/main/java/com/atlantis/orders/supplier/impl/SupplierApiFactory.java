package com.atlantis.orders.supplier.impl;

import com.atlantis.orders.supplier.ISupplierApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class SupplierApiFactory {

    List<ISupplierApi> suppliersApiList;

    @Autowired
    public SupplierApiFactory(List<ISupplierApi> suppliersApiList) {
        this.suppliersApiList = suppliersApiList;
    }

    public ISupplierApi getSupplierApi(Integer supplierId) {

        return suppliersApiList.stream()
                .filter(e -> Objects.equals(e.getSupplierId(), supplierId))
                .findFirst()
                .orElse(null);
    }
}
