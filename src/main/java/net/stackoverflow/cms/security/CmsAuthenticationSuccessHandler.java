package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.constant.RedisPrefixConst;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.util.JsonUtils;
import net.stackoverflow.cms.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功handler
 *
 * @author 凉衫薄
 */
@Component
@Slf4j
public class CmsAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CmsUserDetails userDetails = (CmsUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Result<Object> result = null;

        Long ttl = -1L;
        Boolean lock = (Boolean) redisTemplate.opsForValue().get(RedisPrefixConst.LOCK_PREFIX + user.getId() + ":" + request.getRemoteAddr());
        if (lock != null) {
            ttl = redisTemplate.getExpire(RedisPrefixConst.LOCK_PREFIX + user.getId() + ":" + request.getRemoteAddr());
            result = Result.failure("该用户已被锁定，剩余时间：" + ttl + "秒");
        } else {
            Integer count = redisTemplate.keys(RedisPrefixConst.TOKEN_PREFIX + user.getId() + ":*").size();
            if (count < user.getLimit()) {
                Map<String, String> jwt = TokenUtils.generateToken(user.getId());
                redisTemplate.delete(RedisPrefixConst.FAILURE_PREFIX + user.getId() + ":" + request.getRemoteAddr());
                redisTemplate.opsForValue().set(RedisPrefixConst.TOKEN_PREFIX + user.getId() + ":" + jwt.get("ts"), authentication, user.getTtl(), TimeUnit.MINUTES);
                result = Result.success("登录成功", Base64.getEncoder().encodeToString(JsonUtils.bean2json(jwt).getBytes()));
            } else {
                result = Result.failure("超过登录限制");
            }
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        out.write(JsonUtils.bean2json(result));
        out.flush();
        out.close();

    }
}
