package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.constant.RedisPrefixConst;
import net.stackoverflow.cms.util.JsonUtils;
import net.stackoverflow.cms.util.TokenUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 注销成功handler
 *
 * @author 凉衫薄
 */
@Slf4j
public class CmsLogoutSuccessHandler implements LogoutSuccessHandler {

    private RedisTemplate redisTemplate;

    public CmsLogoutSuccessHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String token = TokenUtils.obtainToken(request);
        if (token != null) {
            redisTemplate.delete(RedisPrefixConst.TOKEN_PREFIX + token);
        }

        Result result = new Result();
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("注销成功");

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        out.write(JsonUtils.bean2json(result));
        out.flush();
        out.close();
    }
}
