package com.daocloud.filter;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String uri = httpServletRequest.getServletPath();
        if("/login".equals(uri)&&SecurityContextHolder.getContext().getAuthentication()!=null){
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setContentType("text/html");
            httpServletResponse.getWriter().println("已登录无法重复登录！");
        }else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}