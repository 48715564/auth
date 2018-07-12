package com.daocloud.config;

import com.daocloud.filter.IntegrationAuthenticationFilter;
import com.daocloud.filter.LoginFilter;
import com.daocloud.filter.LogoutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class OAuthWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private IntegrationAuthenticationFilter integrationAuthenticationFilter;
    @Autowired
    private LogoutFilter logoutFilter;
    @Autowired
    private LoginFilter loginFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.requestMatchers()
                .antMatchers("/oauth/**","/login/**","/logout/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll().and()
                .logout();
        http.addFilterAfter(integrationAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(logoutFilter,UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(loginFilter,UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
