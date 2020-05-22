package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.util.JsonUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * 登陆成功handler
 *
 * @author 凉衫薄
 */
@Slf4j
public class CmsAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedisTemplate redisTemplate;

    public CmsAuthenticationSuccessHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        CmsUserDetails userDetails = (CmsUserDetails) authentication.getPrincipal();
        session.setAttribute("user", userDetails);
        log.info("{}:登录成功", userDetails.getUsername());

        String token = DigestUtils.md5DigestAsHex((userDetails.getId() + System.currentTimeMillis()).getBytes());
        redisTemplate.opsForValue().set(token, userDetails.getId(), 30, TimeUnit.MINUTES);

        Result result = new Result();
        result.setStatus(Result.Status.SUCCESS);
        result.setData(token);
        result.setMessage("登录成功");

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        out.write(JsonUtils.bean2json(result));
        out.flush();
        out.close();

    }
}
