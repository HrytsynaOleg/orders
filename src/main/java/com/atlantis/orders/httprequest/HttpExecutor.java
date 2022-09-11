package com.atlantis.orders.httprequest;

import com.atlantis.orders.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class HttpExecutor {
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public static <T> HttpResponseWrapper<T> executePost(HttpRequestRequest request, TypeReference<T> typeReference) {
        String bodyJson = JsonUtils.convertObjectToJson(request.getBody());
        HttpEntity<Object> httpEntity = new HttpEntity<>(bodyJson, request.getHeaders());
        ResponseEntity<String> response = REST_TEMPLATE.postForEntity(request.getUrl(), httpEntity, String.class);
        T responseBody = JsonUtils.parseJson(response.getBody(), typeReference);
        HttpStatus statusCode = response.getStatusCode();
        return new HttpResponseWrapper<>(statusCode, responseBody);
    }

    public static <T> HttpResponseWrapper<T> executeGet(HttpRequestRequest request, TypeReference<T> typeReference) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(request.getParameters(), request.getHeaders());
        ResponseEntity<String> response = REST_TEMPLATE.exchange(request.getUrl(), HttpMethod.GET, httpEntity, String.class);
        T responseBody = JsonUtils.parseJson(response.getBody(), typeReference);
        HttpStatus statusCode = response.getStatusCode();
        return new HttpResponseWrapper<T>(statusCode, responseBody);
    }

    public static <T> String executeGetBody(HttpRequestRequest request, TypeReference<T> typeReference) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(request.getParameters(), request.getHeaders());
        ResponseEntity<String> response = REST_TEMPLATE.exchange(request.getUrl(), HttpMethod.GET, httpEntity, String.class);
        return response.getBody();
    }

    public static <T> byte[] executeGetByte(HttpRequestRequest request, TypeReference<T> typeReference) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(request.getParameters(), request.getHeaders());
        ResponseEntity<byte[]> response = REST_TEMPLATE.exchange(request.getUrl(), HttpMethod.GET, httpEntity, byte[].class);
        return response.getBody();
    }

    public static HttpResponseWrapper executePostByte(HttpRequestRequest request) {

        HttpEntity<Object> httpEntity = new HttpEntity<>(request.getParameters(), request.getHeaders());
        try {
            ResponseEntity<byte[]> response = REST_TEMPLATE.exchange(request.getUrl(), HttpMethod.POST, httpEntity, byte[].class);
            byte[] content = response.getBody();
            HttpStatus statusCode = response.getStatusCode();
            return new HttpResponseWrapper(statusCode, content);

        }
        catch (HttpClientErrorException ex){
            HttpStatus statusCode = HttpStatus.NOT_FOUND;
            byte[] content = null;
            return new HttpResponseWrapper(statusCode, content);
        }

    }
}