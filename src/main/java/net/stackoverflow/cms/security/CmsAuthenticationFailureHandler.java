package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.util.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
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
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.error(exception.getMessage());

        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE) || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            Result result = new Result();
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("登录失败");
            out.write(JsonUtils.bean2json(result));
            out.flush();
            out.close();
        } else {
            response.sendRedirect("/login");
        }
    }
}