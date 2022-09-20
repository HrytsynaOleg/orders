package com.atlantis.orders.onebox;

import com.atlantis.orders.httprequest.HttpExecutor;
import com.atlantis.orders.httprequest.HttpRequestRequest;
import com.atlantis.orders.httprequest.HttpResponseWrapper;
import com.atlantis.orders.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class OneboxApiRequest {
    @Autowired
    static IOneboxApiSecurityService securityService;

    public static String getHttpPostRequest(String url, String token, Object body) {
        Map<String, Object> response;
        if ((response = getPostRequest(url, token, body)) == null) return "";
        Object dataArray;
        try {
            dataArray = response.get("dataArray");
        } catch (UnsupportedOperationException ex) {
            dataArray = response.get("errorArray");
            String responseJson = JsonUtils.convertObjectToJson(dataArray);
            if (responseJson.contains("wrong token")) {
                token = securityService.refreshToken();
                if ((response = getPostRequest(url, token, body)) == null)
                    return "";
                dataArray = response.get("dataArray");
            }
        }
        return JsonUtils.convertObjectToJson(dataArray);
    }

    private static Map<String, Object> getPostRequest(String url, String token, Object body) {
        HttpRequestRequest httpRequest = HttpRequestRequest.builder()
                .withUrl(url)
                .withBody(body)
                .withHeaderValue("token", token)
                .build();
        HttpResponseWrapper<Map<String, Object>> response = HttpExecutor.executePost(httpRequest, new TypeReference<>() {
        });
        if (response.getStatus().is2xxSuccessful())
            return response.getBody();
        else {
            System.err.printf("Http request error %s", response.getStatus().toString());
            return null;
        }
    }
}
