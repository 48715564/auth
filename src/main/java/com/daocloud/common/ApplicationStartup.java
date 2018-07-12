package com.daocloud.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${getway.secret}")
    private String getwaySecret;
    @Value("${getway.apiUrl}")
    private String getwayAPIUrl;
    @Value("${oauth.timeOut:3}")
    private String expires;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Constant.getwaySecret = getwaySecret;
        Constant.getwayAPIUrl = getwayAPIUrl;
        Constant.expires = expires;
    }
}