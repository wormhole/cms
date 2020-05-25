package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.exception.VerifyCodeException;
import net.stackoverflow.cms.util.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录失败handler
 *
 * @author 凉衫薄
 */
@Slf4j
public class CmsAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        Result result = new Result();
        result.setStatus(Result.Status.FAILURE);
        if (exception instanceof VerifyCodeException) {
            result.setMessage(exception.getMessage());
        } else if (exception instanceof DisabledException) {
            result.setMessage("该用户被禁用");
        } else if (exception instanceof BadCredentialsException) {
            result.setMessage("用户名或密码错误");
        }
        out.write(JsonUtils.bean2json(result));
        out.flush();
        out.close();

    }
}
