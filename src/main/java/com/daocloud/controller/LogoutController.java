package com.daocloud.controller;

import com.daocloud.common.Constant;
import com.daocloud.common.StringUtils;
import com.daocloud.entity.User;
import com.daocloud.service.OauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
public class LogoutController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    TokenStore tokenStore;
    @Autowired
    OauthClientDetailsService oauthClientDetailsService;
    @Autowired
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;

    @RequestMapping("/oauth/exit")
    public String exit(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> logoutList = new LinkedHashSet<>();
        if (o instanceof User) {
            User user = (User) o;
            Set<String> set = stringRedisTemplate.opsForSet().members(Constant.USER_CLIENT + user.getId() + "_" + webAuthenticationDetails.getSessionId());
            for (String clientId : set) {
                Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(clientId, user.getUsername());
                for (OAuth2AccessToken accessToken : tokens) {
                    consumerTokenServices.revokeToken(accessToken.getValue());
                }
                //获得所有的客户端的登出地址
                Map<String, Object> clientMap = oauthClientDetailsService.getOauthClientByClientId(clientId);
                String logoutUri = clientMap.get("logout_uri") == null ? "" : clientMap.get("logout_uri").toString();
                if (StringUtils.isNotBlank(logoutUri)) {
                    logoutList.add(logoutUri);
                }
            }
        }
        modelMap.addAttribute("referer", request.getHeader("referer"));
        modelMap.addAttribute("logoutlist", logoutList);
        return "logout";
    }

    @RequestMapping("/oauth/clearSession")
    @ResponseBody
    public void clearSession(HttpServletRequest request, HttpServletResponse response,String referer) throws IOException {
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (o instanceof User) {
            User user = (User) o;
            stringRedisTemplate.delete(Constant.USER_CLIENT + user.getId() + "_" + webAuthenticationDetails.getSessionId());
        }
        new SecurityContextLogoutHandler().logout(request, null, null);
        if(StringUtils.isNotBlank(referer)&&!"null".equals(referer)){
            response.sendRedirect(referer);
        }else{
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            response.getWriter().println("{\"success\":true,\"msg\":\"退出成功！\"}");
        }
    }
}