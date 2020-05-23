package net.stackoverflow.cms.config;

import net.stackoverflow.cms.security.*;
import net.stackoverflow.cms.service.TokenService;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.web.filter.TokenFilter;
import net.stackoverflow.cms.web.filter.VerifyCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * Spring Security配置类
 *
 * @author 凉衫薄
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();
        http.headers().frameOptions().sameOrigin();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.logout().logoutSuccessHandler(logoutSuccessHandler()).logoutUrl("/logout");
        http.formLogin()
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password");
        http.rememberMe()
                .userDetailsService(userDetailsService())
                .tokenValiditySeconds(60 * 60 * 24 * 30)
                .rememberMeParameter("rememberMe")
                .tokenRepository(tokenRepository());
        http.authorizeRequests()
                .antMatchers("/login/**", "/register", "/code").permitAll()
                .antMatchers("/home/**").authenticated()
                .antMatchers("/dashboard/**").authenticated()
                .antMatchers("/personal/**").authenticated()
                .antMatchers("/auth/**").hasAuthority("auth")
                .antMatchers("/file/**").hasAuthority("file")
                .antMatchers("/config/**").hasAuthority("config")
                .anyRequest().permitAll();
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(authenticationEntryPoint());
        http.addFilterBefore(verifyCodeFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tokenFilter(), LogoutFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public CmsMd5PasswordEncoder passwordEncoder() {
        return new CmsMd5PasswordEncoder();
    }

    @Bean
    public CmsAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CmsAuthenticationSuccessHandler(redisTemplate);
    }

    @Bean
    public CmsAuthenticationFailureHandler authenticationFailureHandler() {
        return new CmsAuthenticationFailureHandler();
    }

    @Bean
    public CmsLogoutSuccessHandler logoutSuccessHandler() {
        return new CmsLogoutSuccessHandler(redisTemplate);
    }

    @Bean
    public CmsAccessDeniedHandler accessDeniedHandler() {
        return new CmsAccessDeniedHandler();
    }

    @Bean
    public CmsAuthenticationEntryPoint authenticationEntryPoint() {
        return new CmsAuthenticationEntryPoint();
    }

    @Bean
    public VerifyCodeFilter verifyCodeFilter() {
        VerifyCodeFilter verifyCodeFilter = new VerifyCodeFilter(authenticationFailureHandler());
        return verifyCodeFilter;
    }

    @Bean
    public TokenFilter tokenFilter() {
        TokenFilter tokenFilter = new TokenFilter(redisTemplate, userDetailsService(), userService);
        return tokenFilter;
    }

    @Bean
    public CmsUserDetailsService userDetailsService() {
        return new CmsUserDetailsService(userService);
    }

    @Bean
    public CmsJdbcTokenRepositoryImpl tokenRepository() {
        return new CmsJdbcTokenRepositoryImpl(tokenService, userService);
    }
}
