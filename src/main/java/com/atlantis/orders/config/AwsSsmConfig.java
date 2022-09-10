package com.atlantis.orders.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;

@Configuration
public class AwsSsmConfig {
    @Bean
    public SsmClient ssmClient() {

        return SsmClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
    }
}
