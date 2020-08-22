package net.stackoverflow.cms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.exception.TokenException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * token工具类
 *
 * @author 凉衫薄
 */
@Slf4j
public class TokenUtils {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String TOKEN_HEADER = "Authorization";
    private static final String ATT_UID = "uid";
    private static final String ATT_TS = "ts";

    /**
     * 从请求头部中获取token
     *
     * @param request
     * @return
     */
    public static String obtainToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if (!StringUtils.isEmpty(token)) {
            if (token.startsWith(TOKEN_PREFIX)) {
                return token.substring(7);
            }
        }
        return null;
    }

    /**
     * 生成token
     *
     * @param id
     * @return
     */
    public static Map<String, String> generateToken(String id) {
        Map<String, String> jwt = new HashMap<>(16);
        jwt.put(ATT_UID, id);
        jwt.put(ATT_TS, String.valueOf(System.currentTimeMillis()));
        return jwt;
    }

    /**
     * 解析json
     *
     * @param token
     * @return
     * @throws JsonProcessingException
     */
    public static Map<String, String> parseToken(String token) throws JsonProcessingException, TokenException {
        String base64 = new String(Base64.getDecoder().decode(token));
        Map<String, String> jwt = (Map<String, String>) JsonUtils.json2bean(base64, Map.class);
        if (jwt.get(ATT_UID) == null || jwt.get(ATT_TS) == null) {
            throw new TokenException("token解析异常");
        }
        return jwt;
    }
}
