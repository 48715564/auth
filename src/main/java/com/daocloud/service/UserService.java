package com.daocloud.service;

import com.daocloud.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public User getUserInfoByUsername(String username){
        Map<String, Object> userMap = jdbcTemplate.queryForMap("SELECT * FROM user WHERE username=?",username);
        User user = new User();
        user.setId(String.valueOf(userMap.get("id")));
        user.setName(String.valueOf(userMap.get("name")));
        user.setUsername(String.valueOf(userMap.get("username")));
        user.setPassword(String.valueOf(userMap.get("password")));
        return user;
    }
}
