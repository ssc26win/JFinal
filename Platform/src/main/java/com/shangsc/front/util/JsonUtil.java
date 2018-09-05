package com.shangsc.front.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ssc
 * @Date 2018/8/16 10:53
 * @Desc 用途：
 */
public final class JsonUtil {

    // Thread safe
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * avoid new
     */
    private JsonUtil() {
    }

    /**
     * Convert Object to json string
     *
     * @param obj
     * @return
     */
    public static String obj2Json(Object obj) {
        if (obj != null) {
            try {
                return mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Convert Object to json byte[]
     *
     * @param obj
     * @return
     */
    public static byte[] obj2Byte(Object obj) {
        if (obj != null) {
            try {
                return mapper.writeValueAsBytes(obj);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    /**
     * Convert Object to Map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> obj2Map(Object obj) {
        if (obj != null) {
            try {
                return mapper.readValue(mapper.writeValueAsBytes(obj), Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }

    /**
     * Convert Object to Map
     *
     * @return
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        if (map != null) {
            try {
                return mapper.readValue(obj2Byte(map), clazz);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Convert json string to Object
     *
     * @param json  String JSON字符串
     * @param clazz Class 类型
     * @return Object 解组对象
     * @throws Exception 异常
     */
    public static <T> T readValue(String json, Class<?> clazz) {
        T t = null;
        try {
            return (T) mapper.readValue(json, clazz);
        } catch (Exception e) {

        }
        return t;
    }

    /**
     * List<Bean> beanList = mapper.readValue(jsonString, new TypeReference<List<Bean>>() {});
     *
     * @param json String JSON字符串
     * @param type TypeReference 类型
     * @return Object 解组对象
     * @throws Exception 异常
     * @todo
     */
    public static <T> T readValue(String json, TypeReference type) {

        T object = null;
        try {
            object = (T) mapper.readValue(json, type);

        } catch (Exception e) {
            // TODO
        }

        return object;
    }

    /**
     * Input callback and clazz,covert to jsonp
     *
     * @param callback
     * @param clazz
     * @return
     */
    public static String o2Jsonp(String callback, Object clazz) {
        String jsonpStr = "";

        jsonpStr += callback;
        jsonpStr += "(";
        jsonpStr += JsonUtil.obj2Json(clazz);
        jsonpStr += ")";

        return jsonpStr;
    }

}



