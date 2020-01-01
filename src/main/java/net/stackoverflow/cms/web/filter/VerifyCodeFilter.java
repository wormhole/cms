package net.stackoverflow.cms.web.filter;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.exception.VerifyCodeException;
import net.stackoverflow.cms.security.CmsAuthenticationFailureHandler;
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
@Slf4j
public class VerifyCodeFilter extends OncePerRequestFilter {

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Setter
    private CmsAuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("POST".equalsIgnoreCase(request.getMethod()) && pathMatcher.match("/login", request.getServletPath())) {
            String vcode = request.getParameter("vcode");
            String realVcode = (String) request.getSession().getAttribute("vcode");
            if (StringUtils.isBlank(vcode)) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new VerifyCodeException("验证码不能为空"));
                return;
            } else if (!realVcode.equalsIgnoreCase(vcode)) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new VerifyCodeException("验证码错误"));
                return;
            }
        }
        doFilter(request, response, filterChain);
    }
}
