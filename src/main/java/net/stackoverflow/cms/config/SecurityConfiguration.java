package net.stackoverflow.cms.config;

import net.stackoverflow.cms.security.*;
import net.stackoverflow.cms.web.filter.VerifyCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 *
 * @author 凉衫薄
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();

        http.rememberMe().tokenValiditySeconds(60 * 60 * 24 * 30);
        http.logout().logoutSuccessHandler(logoutSuccessHandler());
        http.formLogin().loginProcessingUrl("/login");
        http.authorizeRequests()
                .antMatchers("/login", "/register", "/vcode").permitAll()
                .anyRequest().hasAnyRole("admin");
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(authenticationEntryPoint());
        http.addFilterBefore(verifyCodeFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public CmsMd5PasswordEncoder passwordEncoder() {
        return new CmsMd5PasswordEncoder();
    }

    @Bean
    public CmsAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CmsAuthenticationSuccessHandler();
    }

    @Bean
    public CmsAuthenticationFailureHandler authenticationFailureHandler() {
        return new CmsAuthenticationFailureHandler();
    }

    @Bean
    public CmsLogoutSuccessHandler logoutSuccessHandler() {
        return new CmsLogoutSuccessHandler();
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
    public CmsUsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        CmsUsernamePasswordAuthenticationFilter authenticationFilter = new CmsUsernamePasswordAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        authenticationFilter.setFilterProcessesUrl("/login.do");
        return authenticationFilter;
    }

    @Bean
    public VerifyCodeFilter verifyCodeFilter() {
        VerifyCodeFilter verifyCodeFilter = new VerifyCodeFilter();
        verifyCodeFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return verifyCodeFilter;
    }
}
