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

        Integer count = redisTemplate.keys(RedisPrefixConst.TOKEN_PREFIX + user.getId().substring(0, 32)).size();

        if (count < user.getLimit()) {
            String token = TokenUtils.generateToken(user.getId());

            redisTemplate.delete(RedisPrefixConst.FAILURE_PREFIX + user.getId());
            redisTemplate.delete(RedisPrefixConst.LOCK_PREFIX + user.getId());
            redisTemplate.opsForValue().set(RedisPrefixConst.TOKEN_PREFIX + user.getId() + ":" + token, authentication, user.getTtl(), TimeUnit.MINUTES);

            result = Result.success("登录成功");
        } else {
            result = Result.failure("超过登录限制");
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        out.write(JsonUtils.bean2json(result));
        out.flush();
        out.close();

    }
}
