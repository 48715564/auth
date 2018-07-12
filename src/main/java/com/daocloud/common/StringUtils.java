package com.daocloud.common;

public class StringUtils extends org.apache.commons.lang3.StringUtils{
    public static String[] split(Object o, String separatorChar){
        String[] strings = {};
        if(o!=null){
            return split(o.toString(),separatorChar);
        }
        return strings;
    }
}
