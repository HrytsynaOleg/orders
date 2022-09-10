package com.atlantis.orders.service.impl;

import com.atlantis.orders.service.IAwsSecretService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@Service
public class AwsSecretService implements IAwsSecretService {
    SsmClient ssmClient;

    public AwsSecretService(SsmClient ssmClient) {
        this.ssmClient = ssmClient;
    }

    @Override
    public String getSecretByKey(String key) {
        GetParameterResponse parameter = ssmClient.getParameter(GetParameterRequest.builder()
                .name(key)
                .withDecryption(true)
                .build());

        return parameter.parameter().value();
    }
}
