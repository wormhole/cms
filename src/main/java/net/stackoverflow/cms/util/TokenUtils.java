package net.stackoverflow.cms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.exception.TokenException;
import org.springframework.util.DigestUtils;
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
    private static final String SALT = "fewqEfw34fdqfdaof9(d&";

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
    public static Map<String, String> generateJwt(String id) {
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
        String[] tokens = token.split("\\.");
        if (tokens == null || tokens.length != 2) {
            throw new TokenException("token解析异常");
        }
        String payload = new String(Base64.getDecoder().decode(tokens[0]));
        String signature = new String(Base64.getDecoder().decode(tokens[1]));
        if (!signature.equals(DigestUtils.md5DigestAsHex((payload + SALT).getBytes()))) {
            throw new TokenException("token解析异常");
        }
        Map<String, String> jwt = (Map<String, String>) JsonUtils.json2bean(payload, Map.class);
        if (jwt.get(ATT_UID) == null || jwt.get(ATT_TS) == null) {
            throw new TokenException("token解析异常");
        }
        return jwt;
    }

    /**
     * 添加签名,生成token
     *
     * @param jwt
     * @return
     */
    public static String generateToken(Map<String, String> jwt) throws JsonProcessingException {
        String signature = DigestUtils.md5DigestAsHex((JsonUtils.bean2json(jwt) + SALT).getBytes());
        String token = Base64.getEncoder().encodeToString(JsonUtils.bean2json(jwt).getBytes()) + "." + Base64.getEncoder().encodeToString(signature.getBytes());
        return token;
    }
}
