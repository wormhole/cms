package net.stackoverflow.cms.web.filter;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.security.CmsUserDetails;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.TokenUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
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
            String id = (String) redisTemplate.opsForValue().get(token);
            User user = userService.findById(id);
            if (user != null) {
                CmsUserDetails userDetails = (CmsUserDetails) userDetailsService.loadUserByUsername(user.getUsername());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("token认证成功:{}", userDetails.getUsername());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                redisTemplate.expire(token, 30, TimeUnit.MINUTES);
            }
        }
        doFilter(request, response, filterChain);
    }
}
