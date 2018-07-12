package com.daocloud.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by stephan on 04.07.17.
 */
@Component
public class Provider implements Serializable {

    private static final long serialVersionUID = -3301695478208950415L;
    private ObjectMapper objectMapper;

    public Provider() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public Date now() {
        return new Date();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public boolean isAdmin(String userId){
        if("0".equals(userId)){
            return true;
        }else {
            return false;
        }
    }
}
