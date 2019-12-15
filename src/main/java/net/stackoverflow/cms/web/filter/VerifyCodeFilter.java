package net.stackoverflow.cms.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.InputStream;
import java.util.Map;

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

        if ("POST".equalsIgnoreCase(request.getMethod()) && pathMatcher.match("/login.do", request.getServletPath())) {
            String vcode = null;
            String username = null;
            String password = null;
            boolean rememberMe = false;
            ObjectMapper mapper = new ObjectMapper();
            try {
                InputStream is = request.getInputStream();
                Map authenticationBean = mapper.readValue(is, Map.class);
                vcode = (String) authenticationBean.get("vcode");
                username = (String) authenticationBean.get("username");
                password = (String) authenticationBean.get("password");
                rememberMe = (boolean) authenticationBean.get("rememberMe");
                request.setAttribute("username", username);
                request.setAttribute("password", password);
                request.setAttribute("rememberMe", rememberMe);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
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
