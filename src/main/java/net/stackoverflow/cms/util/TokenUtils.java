package net.stackoverflow.cms.util;

import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * token工具类
 *
 * @author 凉衫薄
 */
public class TokenUtils {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String TOKEN_HEADER = "Authorization";

    /**
     * 从请求头部中获取token
     *
     * @param request
     * @return
     */
    public static String obtainToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if (StringUtils.isNotEmpty(token)) {
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
    public static String generateToken(String id) {
        String salt = String.valueOf(System.currentTimeMillis());
        return DigestUtils.md5DigestAsHex((id + salt).getBytes());
    }

}
