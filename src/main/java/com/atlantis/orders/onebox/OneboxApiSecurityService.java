package com.atlantis.orders.onebox;

import com.atlantis.orders.constants.OneboxApiEndpoints;
import com.atlantis.orders.httprequest.HttpExecutor;
import com.atlantis.orders.httprequest.HttpRequestRequest;
import com.atlantis.orders.httprequest.HttpResponseWrapper;
import com.atlantis.orders.service.IAwsSecretService;
import com.atlantis.orders.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OneboxApiSecurityService implements IOneboxApiSecurityService {

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

        String resultBody;
        Map<String, Object> responseMap;

        HttpRequestRequest httpRequest = HttpRequestRequest.builder()
                .withUrl(OneboxApiEndpoints.ONEBOX_GET_TOKEN)
                .withBody(body)
                .build();
        HttpResponseWrapper<Map<String, Object>> response = HttpExecutor.executePost(httpRequest, new TypeReference<>() {
        });
        if (response.getStatus().is2xxSuccessful())
            responseMap = response.getBody();
        else {
            System.err.printf("Http request error %s", response.getStatus().toString());
            token="";
            return;
        }
        Object dataArray;
        try {
        dataArray = responseMap.get("dataArray");
        } catch (UnsupportedOperationException ex) {
            dataArray = responseMap.get("errorArray");
            String responseJson = JsonUtils.convertObjectToJson(dataArray);
            System.err.printf("Http request error %s", responseJson);
            token="";
            return;
        }
        resultBody = JsonUtils.convertObjectToJson(dataArray);

        if (!resultBody.isEmpty())
            token = JsonUtils.parseJson(resultBody, new TypeReference<Map<String, String>>() {
            }).get("token");
    }
}
