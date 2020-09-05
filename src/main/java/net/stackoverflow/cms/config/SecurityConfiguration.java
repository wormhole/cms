package net.stackoverflow.cms.config;

import net.stackoverflow.cms.security.CmsTokenFilter;
import net.stackoverflow.cms.security.CmsVerifyCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Spring Security配置类
 *
 * @author 凉衫薄
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    @Qualifier("cmsUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;
    @Autowired
    private CmsTokenFilter tokenFilter;
    @Autowired
    private CmsVerifyCodeFilter verifyCodeFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();
        http.headers().frameOptions().sameOrigin();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.logout().logoutSuccessHandler(logoutSuccessHandler).logoutUrl("/logout");
        http.formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password");
        /*http.rememberMe()
                .key("rememberMe")
                .userDetailsService(userDetailsService())
                .tokenValiditySeconds(60 * 60 * 24 * 30)
                .rememberMeParameter("rememberMe");*/
        http.authorizeRequests()
                .antMatchers("/home/**").authenticated()
                .antMatchers("/dashboard/**").authenticated()
                .antMatchers("/personal/**").authenticated()
                //.antMatchers("/auth/**").hasAuthority("auth")
                //.antMatchers("/manage/**").hasAuthority("manage")
                .anyRequest().permitAll();
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint);
        http.addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tokenFilter, LogoutFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
