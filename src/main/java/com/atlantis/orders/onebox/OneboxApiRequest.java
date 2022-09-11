package com.atlantis.orders.onebox;

import com.atlantis.orders.httprequest.HttpExecutor;
import com.atlantis.orders.httprequest.HttpRequestRequest;
import com.atlantis.orders.httprequest.HttpResponseWrapper;
import com.atlantis.orders.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class OneboxApiRequest {

    public static String performHttpRequest(String url, String token, Object body) {
        String result = "";
        HttpRequestRequest httpRequest = HttpRequestRequest.builder()
                .withUrl(url)
                .withBody(body)
                .withHeaderValue("token", token)
                .build();
        HttpResponseWrapper<Map<String, Object>> httpResult = HttpExecutor.executePost(httpRequest, new TypeReference<>() {
        });
        if (httpResult.getStatus().is2xxSuccessful()) {
            Map<String, Object> newTokenResultBody = httpResult.getBody();
            Object newTokenBody = newTokenResultBody.get("dataArray");
            result = JsonUtils.convertObjectToJson(newTokenBody);
        }
        return result;
    }
}
