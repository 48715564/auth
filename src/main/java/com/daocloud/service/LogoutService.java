package com.daocloud.service;

import cn.hutool.http.HttpUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LogoutService {
    @Async
    public void sendLogoutUrl(String url,Map<String,Object> param){
        HttpUtil.get(url,param);
    }
}
