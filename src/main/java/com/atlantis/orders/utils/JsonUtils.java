package com.atlantis.orders.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String convertObjectToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> T readInputStream(InputStream input, Class<T> clazz) throws IOException {
        return mapper.reader().readValue(input, clazz);
    }

    public static <T> T parseBytes(byte[] src, Class<T> clazz) {
        try {
            return mapper.readValue(src, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Parsing error");
        }
    }

    public static <T> T parseJson(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            System.out.printf("Could not parse json: %s%n", json);
        }
        return null;
    }

    public static <T> T parseJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            System.out.printf("Could not parsing json: %s%n", json);
        }
        return null;
    }
}
