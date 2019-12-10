package net.stackoverflow.cms.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Json工具类
 *
 * @author 凉衫薄
 */
@Slf4j
public class JsonUtils {

    public static String bean2json(Object bean) {
        String json = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(bean);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return json;
    }
}
