package com.atlantis.orders.models;

public enum OrderStatus {
    NEW(117),
    SENT(118),
    APPROVED(119),
    WAIT_FOR_RECEIVE(121),
    RECEIVED(122);

    OrderStatus(int statusCode) {
    }


}
