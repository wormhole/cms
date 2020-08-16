package net.stackoverflow.cms.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.constant.RedisPrefixConst;
import net.stackoverflow.cms.exception.TokenException;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token认证过滤器
 *
 * @author 凉衫薄
 */
@Component
@Slf4j
public class CmsTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    @Qualifier("cmsUserDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        log.info("tokenFilter:{}", path);

        String token = TokenUtils.obtainToken(request);
        if (token != null) {
            Map<String, String> jwt = null;
            try {
                jwt = TokenUtils.parseToken(token);
                Authentication authentication = (Authentication) redisTemplate.opsForValue().get(RedisPrefixConst.TOKEN_PREFIX + jwt.get("uid") + ":" + jwt.get("ts"));
                if (authentication != null) {
                    CmsUserDetails userDetails = (CmsUserDetails) authentication.getPrincipal();
                    User user = userService.findById(userDetails.getUser().getId());
                    if (user != null) {
                        CmsUserDetails details = (CmsUserDetails) userDetailsService.loadUserByUsername(user.getUsername());
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                details, null, details.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        log.info("token认证成功:{}", details.getUsername());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        redisTemplate.expire(RedisPrefixConst.TOKEN_PREFIX + jwt.get("uid") + ":" + jwt.get("ts"), 30, TimeUnit.MINUTES);
                    } else {
                        log.error("token认证失败:{}", token);
                        redisTemplate.delete(RedisPrefixConst.TOKEN_PREFIX + jwt.get("uid") + ":" + jwt.get("ts"));
                    }
                } else {
                    log.error("token不存在:{}", token);
                }
            } catch (JsonProcessingException | TokenException e) {
                log.error("解析token异常", e);
            }
        }
        doFilter(request, response, filterChain);
    }
}
