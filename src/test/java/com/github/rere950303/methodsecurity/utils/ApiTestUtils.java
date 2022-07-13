package com.github.rere950303.methodsecurity.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ApiTestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

}