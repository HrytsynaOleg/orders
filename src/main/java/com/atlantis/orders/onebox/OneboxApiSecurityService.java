package com.atlantis.orders.onebox;

import com.atlantis.orders.constants.OneboxApiEndpoints;
import com.atlantis.orders.service.IAwsSecretService;
import com.atlantis.orders.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Component
public class OneboxApiSecurityService implements IOneboxApiSecurityService{

    private static String token;
    private final IAwsSecretService secrets;

    @Autowired
    private OneboxApiSecurityService(IAwsSecretService secrets) {
        this.secrets = secrets;
    }

    public String getToken() {
        if (token == null) requestNewToken();
        return token;
    }
    public String refreshToken() {
        requestNewToken();
        return token;
    }

    private void requestNewToken() {

        Map<String, String> body = new HashMap<>();
        body.put("restapipassword", secrets.getSecretByKey("restapipassword"));
        body.put("login", secrets.getSecretByKey("login"));

        String resultBody = OneboxApiRequest.getHttpPostRequest(OneboxApiEndpoints.ONEBOX_GET_TOKEN, "", body);

        if (!resultBody.isEmpty())
            token = JsonUtils.parseJson(resultBody, new TypeReference<Map<String, String>>() {
            }).get("token");
    }
}
