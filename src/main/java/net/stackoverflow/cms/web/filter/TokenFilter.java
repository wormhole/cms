package net.stackoverflow.cms.web.filter;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.constant.RedisPrefixConst;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.security.CmsUserDetails;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.TokenUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * token认证过滤器
 *
 * @author 凉衫薄
 */
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;
    private UserDetailsService userDetailsService;
    private UserService userService;

    private AuthenticationFailureHandler authenticationFailureHandler;

    public TokenFilter(RedisTemplate redisTemplate, UserDetailsService userDetailsService, UserService userService) {
        this.redisTemplate = redisTemplate;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        log.info("tokenFilter:{}", path);

        String token = TokenUtils.obtainToken(request);
        if (token != null) {
            Authentication authentication = (Authentication) redisTemplate.opsForValue().get(RedisPrefixConst.TOKEN_PREFIX + token);
            if (authentication != null) {
                CmsUserDetails userDetails = (CmsUserDetails) authentication.getPrincipal();
                User user = userService.findById(userDetails.getId());
                if (user != null) {
                    CmsUserDetails details = (CmsUserDetails) userDetailsService.loadUserByUsername(user.getUsername());
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            details, null, details.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("token认证成功:{}", details.getUsername());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    redisTemplate.expire(RedisPrefixConst.TOKEN_PREFIX + token, 30, TimeUnit.MINUTES);
                } else {
                    log.error("token认证失败:{}", token);
                    redisTemplate.delete(RedisPrefixConst.TOKEN_PREFIX + token);
                }
            } else {
                log.error("token不存在:{}", token);
            }
        }
        doFilter(request, response, filterChain);
    }
}
