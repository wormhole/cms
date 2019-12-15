package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义RememberMeService
 *
 * @author 凉衫薄
 */
@Slf4j
public class CmsRememberMeService extends PersistentTokenBasedRememberMeServices {
    public CmsRememberMeService(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {

        Boolean rememberMe = (Boolean) request.getAttribute("rememberMe");

        return rememberMe != null && rememberMe == true;
    }

}
