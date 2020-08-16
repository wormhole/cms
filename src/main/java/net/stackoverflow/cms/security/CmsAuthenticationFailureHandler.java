package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.constant.RedisPrefixConst;
import net.stackoverflow.cms.exception.VerifyCodeException;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * 登录失败handler
 *
 * @author 凉衫薄
 */
@Component
@Slf4j
public class CmsAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        Result<Object> result = null;
        if (exception instanceof VerifyCodeException) {
            result = Result.failure("验证码错误");
        } else if (exception instanceof DisabledException) {
            result = Result.failure("该用户被禁用");
        } else if (exception instanceof LockedException) {

        } else if (exception instanceof BadCredentialsException) {
            String username = request.getParameter("username");
            User user = userService.findByUsername(username);
            if (user != null) {
                Long ttl = -1L;
                Boolean lock = (Boolean) redisTemplate.opsForValue().get(RedisPrefixConst.LOCK_PREFIX + user.getId() + ":" + request.getRemoteAddr());
                if (lock != null) {
                    ttl = redisTemplate.getExpire(RedisPrefixConst.LOCK_PREFIX + user.getId() + ":" + request.getRemoteAddr());
                    result = Result.failure("该用户已被锁定，剩余时间：" + ttl + "秒");
                } else {
                    Long count = redisTemplate.opsForValue().increment(RedisPrefixConst.FAILURE_PREFIX + user.getId() + ":" + request.getRemoteAddr());
                    if (count >= user.getFailure()) {
                        redisTemplate.opsForValue().set(RedisPrefixConst.LOCK_PREFIX + user.getId() + ":" + request.getRemoteAddr(), true, user.getLock(), TimeUnit.MINUTES);
                        redisTemplate.delete(RedisPrefixConst.FAILURE_PREFIX + user.getId() + ":" + request.getRemoteAddr());
                        result = Result.failure("登录失败次数超过" + user.getFailure() + "次，已被锁定");
                    } else {
                        result = Result.failure("登录失败" + count + "次，还有" + (user.getFailure() - count) + "次机会");
                    }
                }
            } else {
                result = Result.failure("无此用户");
            }
        }
        out.write(JsonUtils.bean2json(result));
        out.flush();
        out.close();

    }
}
