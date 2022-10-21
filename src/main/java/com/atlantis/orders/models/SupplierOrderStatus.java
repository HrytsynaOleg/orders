package com.atlantis.orders.models;

public enum SupplierOrderStatus {
    NEW(117),
    NEW_DROPSHIP(169),
    SENT(118),
    APPROVED(119),
    WAIT_FOR_RECEIVE(121),
    WAIT_FOR_DROPSHIP(172),
    RECEIVED_DROPSHIP(173),
    RECEIVED(122);

    final int statusCode;

    SupplierOrderStatus(int statusCode) {
        this.statusCode=statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
