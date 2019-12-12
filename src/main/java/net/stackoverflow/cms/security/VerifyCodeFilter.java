package net.stackoverflow.cms.security;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Setter;
import net.stackoverflow.cms.exception.VerifyCodeException;
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
public class VerifyCodeFilter extends OncePerRequestFilter {

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Setter
    private CmsAuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("POST".equalsIgnoreCase(request.getMethod()) && pathMatcher.match("/login.do", request.getServletPath())) {
            String vcode = request.getParameter("vcode");
            String realVcode = (String) request.getSession().getAttribute("vcode");
            if (StringUtils.isBlank(vcode) || !realVcode.equalsIgnoreCase(vcode)) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new VerifyCodeException("验证码不能为空或验证码错误"));
                return;
            }
        }
        doFilter(request, response, filterChain);
    }
}
