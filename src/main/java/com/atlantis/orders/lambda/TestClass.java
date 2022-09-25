package com.atlantis.orders.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class TestClass implements RequestHandler<String, String> {
    @Override
    public String handleRequest(String o, Context context) {
        System.out.printf("Test %s ", o);
        return null;
    }
}
