package com.atlantis.orders.onebox;

import com.atlantis.orders.httprequest.HttpExecutor;
import com.atlantis.orders.httprequest.HttpRequestRequest;
import com.atlantis.orders.httprequest.HttpResponseWrapper;
import com.atlantis.orders.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class OneboxApiRequest {

    private static IOneboxApiSecurityService securityService;

    @Autowired
    private IOneboxApiSecurityService tmpSecurityService;

//    @Autowired
//    public OneboxApiRequest(IOneboxApiSecurityService securityService) {
//        OneboxApiRequest.securityService = securityService;
//    }

    public static String getHttpPostRequest(String url, Object body) {
        String token = securityService.getToken();
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

    @PostConstruct
    public void init() {
        securityService = tmpSecurityService;
    }
}
