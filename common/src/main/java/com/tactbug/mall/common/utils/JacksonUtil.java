package com.tactbug.mall.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tactbug.mall.common.base.TactException;
import com.tactbug.mall.common.exceptions.TactJsonException;

public class JacksonUtil {

    private static final ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
    }

    public static<T> String objectToString(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new TactJsonException("序列化失败[" + t.toString() + "]");
        }
    }

    public static<T> T stringToObject(String s, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(s, typeReference);
        } catch (JsonProcessingException e) {
            throw new TactJsonException("反序列化失败[" + s + "]");
        }
    }

}
