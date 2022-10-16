package com.atlantis.orders.models;

public enum SupplierOrderStatus {
    NEW(117),
    SENT(118),
    APPROVED(119),
    WAIT_FOR_RECEIVE(121),
    RECEIVED(122);

    final int statusCode;

    SupplierOrderStatus(int statusCode) {
        this.statusCode=statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
