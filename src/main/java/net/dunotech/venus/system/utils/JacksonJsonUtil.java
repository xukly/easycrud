package net.dunotech.venus.system.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

public class JacksonJsonUtil {
    private static ObjectMapper mapper;

    public JacksonJsonUtil() {
    }

    public static synchronized ObjectMapper getMapperInstance(boolean createNew) {
        if (createNew) {
            return new ObjectMapper();
        } else {
            if (mapper == null) {
                mapper = new ObjectMapper();
            }

            return mapper;
        }
    }

    public static String beanToJsonByCharset(Object obj) {
        return beanToJson(obj, false, "ISO-8859-1");
    }

    public static String beanToJson(Object obj) {
        return beanToJson(obj, false, (String)null);
    }

    public static String beanToJson(Object obj, boolean createNew, String chartset) {
        try {
            ObjectMapper objectMapper = getMapperInstance(createNew);
            String json = objectMapper.writeValueAsString(obj);
            String result = json;
            if (!StringUtils.isEmpty(chartset)) {
                result = new String(json.getBytes("UTF-8"), "ISO-8859-1");
            }

            return result;
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    public static Object jsonToBean(String json, Class<?> cls) {
        try {
            ObjectMapper objectMapper = getMapperInstance(false);
            Object vo = objectMapper.readValue(json, cls);
            return vo;
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static Object jsonToBean(String json, Class<?> cls, Boolean createNew) {
        try {
            ObjectMapper objectMapper = getMapperInstance(createNew);
            Object vo = objectMapper.readValue(json, cls);
            return vo;
        } catch (Exception var5) {
            throw new RuntimeException(var5);
        }
    }

    public static Object jsonToBean(String json, TypeReference<?> t) {
        try {
            ObjectMapper objectMapper = getMapperInstance(false);
            Object vo = objectMapper.readValue(json, t);
            return vo;
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    public static <T> T jsonToBean(String jsonStr, Class<?> collectionClass, Class... elementClasses) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
            T vo = mapper.readValue(jsonStr, javaType);
            return vo;
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }
}
