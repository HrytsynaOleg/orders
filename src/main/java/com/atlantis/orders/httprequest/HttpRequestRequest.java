package com.atlantis.orders.httprequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestRequest {

    private String url;
    private Map<String, Object> parameters;
    private HttpHeaders headers;
    private String bodyJson;
    private Object body;

    public HttpRequestRequest(String url, Map<String, Object> parameters, HttpHeaders headers, String bodyJson,Object body) {
        this.url = url;
        this.parameters = parameters;
        this.headers = headers;
        this.bodyJson=bodyJson;
        this.body=body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private String url;
        private Map<String, Object> parameters;
        private HttpHeaders headers;
        private String bodyJson;
        private Object body;

        public Builder() {
            this.parameters = new HashMap<>();
            this.headers = new HttpHeaders();
        }

        public Builder withUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withParameter(String name, Object value) {
            this.parameters.put(name, value);
            return this;
        }

        public Builder withBodyJson(String json) {
            this.bodyJson = json;
            return this;
        }
        public Builder withBody(Object body) {
            this.body = body;
            return this;
        }


        public Builder withHeaderValue(String headerName, String headerValue) {
            this.headers.set(headerName, headerValue);
            return this;
        }

        public Builder withContent(MediaType type) {
            this.headers.setContentType(type);
            return this;
        }

        public Builder withAccept(List<MediaType> typesList) {
            this.headers.setAccept(typesList);
            return this;
        }

        public HttpRequestRequest build() {
            return new HttpRequestRequest(url, parameters, headers, bodyJson,body);
        }
    }

    public String getBodyJson() {
        return bodyJson;
    }

    public void setBodyJson(String bodyJson) {
        this.bodyJson = bodyJson;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
