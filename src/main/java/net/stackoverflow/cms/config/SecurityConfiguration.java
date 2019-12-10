package net.stackoverflow.cms.config;

import net.stackoverflow.cms.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        http.logout().logoutSuccessUrl("/");
        http.rememberMe().tokenValiditySeconds(60 * 60 * 24 * 30);
        http.formLogin().loginPage("/login");
        http.authorizeRequests()
                .antMatchers("/login", "/login.do", "/register", "/vcode", "/favicon.icon", "/static/**").permitAll()
                .anyRequest().hasAnyRole("admin");
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
        http.addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
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
    public CmsAccessDeniedHandler accessDeniedHandler() {
        return new CmsAccessDeniedHandler();
    }

    @Bean
    public CmsAuthenticationFilter authenticationFilter() throws Exception {
        CmsAuthenticationFilter authenticationFilter = new CmsAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        authenticationFilter.setFilterProcessesUrl("/login.do");
        return authenticationFilter;
    }
}
