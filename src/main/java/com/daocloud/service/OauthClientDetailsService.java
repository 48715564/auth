package com.daocloud.service;

import com.daocloud.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OauthClientDetailsService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Map<String,Object> getOauthClientByClientId(String clientId){
        Map<String, Object> userMap = jdbcTemplate.queryForMap("SELECT * FROM oauth_client_details WHERE client_id=?",clientId);
        return userMap;
    }
}
