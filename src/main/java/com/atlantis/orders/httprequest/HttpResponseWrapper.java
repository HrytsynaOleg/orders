package com.atlantis.orders.httprequest;

import org.springframework.http.HttpStatus;

public class HttpResponseWrapper <T> {

    private HttpStatus status;
    private T body;
    private byte[] content;

    public HttpResponseWrapper(HttpStatus status, T body) {
        this.status = status;
        this.body = body;
    }

    public HttpResponseWrapper(HttpStatus status, byte[] content) {
        this.status = status;
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

}
