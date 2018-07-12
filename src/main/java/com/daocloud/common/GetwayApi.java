package com.daocloud.common;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GetwayApi {

    private static final String CREATE_KEY = "/tyk/keys/create";

    private static final String REMOVE_KEY = "/tyk/keys/{access_token}";

    private static final String AUTHORIZATION = "x-tyk-authorization";

    public static Map<String, String> getAuthorization() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(AUTHORIZATION, Constant.getwaySecret);
        return map;
    }

    public static String getAccessRights(List<Map<String, Object>> apiList) {
        JSONObject jsonObject = new JSONObject();
        for (Map<String, Object> map : apiList) {
            Map<String, Object> tempMap = new LinkedHashMap<>();
            tempMap.put("api_id", map.get("api_id"));
            tempMap.put("api_name", map.get("api_name"));
            tempMap.put("versions", StringUtils.split(map.get("api_version"), ","));
            JSONArray jsonArray = new JSONArray();
            Object allowed_urls = map.get("allowed_urls");
            if(allowed_urls!=null&&StringUtils.isNotBlank(allowed_urls.toString())){
                jsonArray = JSONUtil.parseArray(map.get("allowed_urls"));
            }
            tempMap.put("allowed_urls",jsonArray);
            jsonObject.put(map.get("api_id").toString(), tempMap);
        }
        return JSONUtil.toJsonStr(jsonObject);
    }

    public static void main(String[] args) {
        System.out.println(JSONUtil.toJsonStr(StringUtils.split("", ",")));
    }

    public static void deleteKey(){

    }

    public static String createKey(List<Map<String, Object>> apiList) {
        String requestBody = "{\n" +
                "\t\"last_check\": 0,\n" +
                "\t\"certificate\": null,\n" +
                "\t\"allowance\": 1000,\n" +
                "\t\"hmac_enabled\": false,\n" +
                "\t\"hmac_string\": \"\",\n" +
                "\t\"basic_auth_data\": {\n" +
                "\t\t\"password\": \"\"\n" +
                "\t},\n" +
                "\t\"rate\": 1000,\n" +
                "\t\"per\": 60,\n" +
                "\t\"expires\": "+DateUtils.getOffsetDay(Integer.valueOf(Constant.expires))+",\n" +
                "\t\"quota_max\": -1,\n" +
                "\t\"quota_renews\": 1529992013,\n" +
                "\t\"quota_remaining\": -1,\n" +
                "\t\"quota_renewal_rate\": -1,\n" +
                "\t\"access_rights\": " + getAccessRights(apiList) + "," +
                "\t\"apply_policy_id\": \"\",\n" +
                "\t\"apply_policies\": [],\n" +
                "\t\"tags\": [],\n" +
                "\t\"jwt_data\": {\n" +
                "\t\t\"secret\": \"\"\n" +
                "\t},\n" +
                "\t\"meta_data\": {},\n" +
                "\t\"alias\": \"\"\n" +
                "}";
        return HttpRequest.post(Constant.getwayAPIUrl + CREATE_KEY).addHeaders(getAuthorization()).body(requestBody).execute().body(); }
}
