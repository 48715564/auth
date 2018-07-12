package com.daocloud.filter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogoutFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String uri = httpServletRequest.getServletPath();
        if("/oauth/exit".equals(uri)&&SecurityContextHolder.getContext().getAuthentication()==null){
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setContentType("text/html");
            httpServletResponse.getWriter().println("已退出登录无法再退出！");
        }else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}