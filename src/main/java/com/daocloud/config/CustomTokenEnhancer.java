package com.daocloud.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.daocloud.common.Constant;
import com.daocloud.common.GetwayApi;
import com.daocloud.common.StringUtils;
import com.daocloud.entity.User;
import com.daocloud.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {
    @Autowired
    private ApiService apiService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String tokenStr = "";
        if (accessToken instanceof DefaultOAuth2AccessToken) {
            String grantType = authentication.getOAuth2Request().getGrantType();
//            生成对应的tyk凭证
            List<Map<String, Object>> apiList = apiService.getApiListByClientID(authentication.getOAuth2Request().getClientId());
            if (apiList != null && apiList.size() > 0) {
                String key = GetwayApi.createKey(apiList);
                JSONObject jsonObject = JSONUtil.parseObj(key);
                if (jsonObject != null && "ok".equals(jsonObject.getStr("status"))) {
                    tokenStr = jsonObject.getStr("key");
                } else {
                    throw new RuntimeException(jsonObject.getStr("message"));
                }
            }
            if (StringUtils.isBlank(tokenStr)) {
                tokenStr = getNewToken();
            }
            DefaultOAuth2AccessToken token = ((DefaultOAuth2AccessToken) accessToken);
            token.setValue(tokenStr);

            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken instanceof DefaultOAuth2RefreshToken) {
                token.setRefreshToken(new DefaultOAuth2RefreshToken(getNewToken()));
            }
            return token;
        }
        return accessToken;
    }

    private String getNewToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}  