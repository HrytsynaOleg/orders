package com.atlantis.orders.onebox;

import org.springframework.stereotype.Component;

@Component
public interface IOneboxApiSecurityService {
    String getToken();
    String refreshToken();
}
