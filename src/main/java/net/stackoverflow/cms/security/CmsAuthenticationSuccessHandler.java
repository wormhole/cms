package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.util.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登陆成功handler
 *
 * @author 凉衫薄
 */
@Slf4j
public class CmsAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        session.setAttribute("user", userDetails);
        log.info(userDetails.getUsername() + ":登录成功");
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE) || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            Result result = new Result();
            result.setStatus(Result.Status.SUCCESS);
            result.setMessage("登录成功");
            PrintWriter out = response.getWriter();
            out.write(JsonUtils.bean2json(result));
            out.flush();
            out.close();
        } else {
            log.info("重定向到:" + request.getContextPath());
            response.sendRedirect(request.getContextPath());
        }
    }
}
