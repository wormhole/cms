package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.exception.CaptchaException;
import net.stackoverflow.cms.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录验证码过滤器
 *
 * @author 凉衫薄
 */
@Component
@Slf4j
public class CmsCaptchaFilter extends OncePerRequestFilter {

    private PathMatcher pathMatcher = new AntPathMatcher();
    private static final String METHOD = "POST";
    private static final String URL = "/login";

    @Autowired
    private CmsAuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (METHOD.equalsIgnoreCase(request.getMethod()) && pathMatcher.match(URL, request.getServletPath())) {
            String captcha = request.getParameter("captcha");
            String real = (String) request.getSession().getAttribute("captcha");
            if (StringUtils.isBlank(captcha)) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new CaptchaException("验证码不能为空"));
                return;
            } else if (StringUtils.isBlank(real)) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new CaptchaException("验证码失效"));
                return;
            } else if (!real.equalsIgnoreCase(captcha)) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new CaptchaException("验证码错误"));
                return;
            }
        }
        doFilter(request, response, filterChain);
    }
}
