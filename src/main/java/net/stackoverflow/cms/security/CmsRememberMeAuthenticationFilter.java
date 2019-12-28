package net.stackoverflow.cms.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义记住我过滤器
 *
 * @author 凉衫薄
 */
public class CmsRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter {
    public CmsRememberMeAuthenticationFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {
        super(authenticationManager, rememberMeServices);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        request.getSession().setAttribute("user", authResult.getPrincipal());
        super.onSuccessfulAuthentication(request, response, authResult);
    }
}
