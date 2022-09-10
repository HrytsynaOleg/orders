package com.atlantis.orders.service;

import org.springframework.stereotype.Component;

@Component
public interface IAwsSecretService {
    String getSecretByKey(String key);
}
