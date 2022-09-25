package com.atlantis.orders.lambda;

import com.atlantis.orders.lambda.lambdaSecond.SimpleCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SimpleWriter {

    private final SimpleCreator simpleCreator;

    @Autowired
    public SimpleWriter(SimpleCreator simpleCreator) {
        this.simpleCreator = simpleCreator;
    }


    public String getMessage() {

        return simpleCreator.createString();
    }
}
