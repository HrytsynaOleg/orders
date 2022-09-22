package com.atlantis.orders.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.stereotype.Component;

@Component
@EnableScan
public interface IBrandSuffixDynamoDbRepository {
    String getSuffix(String brand);
}
