package net.stackoverflow.cms.config;

import net.stackoverflow.cms.security.LoginSuccessHandler;
import net.stackoverflow.cms.security.Md5PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login.do")
                .failureUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler());
        http.authorizeRequests()
                .antMatchers("/login", "/login.do", "/register", "/vcode", "/favicon.icon", "/static/**").permitAll()
                .anyRequest().authenticated();
        http.logout().logoutSuccessUrl("/");
        http.rememberMe().tokenValiditySeconds(60 * 60 * 24 * 30);
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
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }
}
