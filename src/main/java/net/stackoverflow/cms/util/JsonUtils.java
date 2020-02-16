package net.stackoverflow.cms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Json工具类
 *
 * @author 凉衫薄
 */
@Slf4j
public class JsonUtils {

    /**
     * bean转json
     *
     * @param bean
     * @return
     * @throws JsonProcessingException
     */
    public static String bean2json(Object bean) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bean);
        return json;
    }

    /**
     * json转bean
     *
     * @param json
     * @param clazz
     * @return
     * @throws JsonProcessingException
     */
    public static Object json2bean(String json, Class clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Object object = mapper.readValue(json, clazz);
        return object;
    }
}
