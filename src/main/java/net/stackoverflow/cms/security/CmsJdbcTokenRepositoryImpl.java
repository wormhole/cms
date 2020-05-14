package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.model.entity.Token;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.service.TokenService;
import net.stackoverflow.cms.service.UserService;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * JdbcToken实现类
 *
 * @author 凉衫薄
 */
@Slf4j
public class CmsJdbcTokenRepositoryImpl implements PersistentTokenRepository {

    private TokenService tokenService;
    private UserService userService;

    public CmsJdbcTokenRepositoryImpl(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        log.info("创建token:{}", token.getUsername());
        Token tk = new Token();
        tk.setId(token.getSeries());
        tk.setToken(token.getTokenValue());
        tk.setLastUsed(token.getDate());
        User user = userService.findByUsername(token.getUsername());
        tk.setUserId(user.getId());
        tokenService.save(tk);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        log.info("更新token:{}", series);
        Token tk = tokenService.findById(series);
        tk.setToken(tokenValue);
        tk.setLastUsed(lastUsed);
        tokenService.update(tk);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        Token tk = tokenService.findById(seriesId);
        if (tk != null) {
            User user = userService.findById(tk.getUserId());
            return new PersistentRememberMeToken(user.getUsername(), tk.getId(), tk.getToken(), tk.getLastUsed());
        } else {
            return null;
        }
    }

    @Override
    public void removeUserTokens(String username) {
        log.info("移除token:{}", username);
        User user = userService.findByUsername(username);

        List<Token> tokens = new ArrayList<>();
        if (user != null) {
            tokens = tokenService.findByCondition(new HashMap<String, Object>() {{
                put("userId", user.getId());
            }});
        }
        if (tokens.size() > 0) {
            tokenService.delete(tokens.get(0).getId());
        }
    }
}
