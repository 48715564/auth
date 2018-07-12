package com.daocloud.filter;

import com.daocloud.common.Constant;
import com.daocloud.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class IntegrationAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            //记录用户登录客户端信息
            if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2Authentication) {
                OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
                    User user = (User) authentication.getPrincipal();
                    WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getUserAuthentication().getDetails();
                    stringRedisTemplate.opsForSet().add(Constant.USER_CLIENT + user.getId() + "_" + webAuthenticationDetails.getSessionId(), authentication.getOAuth2Request().getClientId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}