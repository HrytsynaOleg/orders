package com.atlantis.orders.lambda.lambdaSecond;

import org.springframework.stereotype.Component;

@Component
public class SimpleCreator {

    public String createString(){
        return "My created message";
    }
}
