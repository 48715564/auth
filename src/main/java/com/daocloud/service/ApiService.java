package com.daocloud.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    public List<Map<String,Object>> getApiListByClientID(String clientID){
        List<Map<String,Object>> list = jdbcTemplate.queryForList("SELECT * FROM api_client_role WHERE client_id=?",clientID);
        return list;
    }
}
