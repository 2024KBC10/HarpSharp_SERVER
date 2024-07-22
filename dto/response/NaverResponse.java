package com.harpsharp.auth.dto.response;

import com.harpsharp.auth.oauth2.OAuth2Response;

import java.util.Map;


public class NaverResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute){
        this.attribute = (Map<String, Object>) attribute.get("response");
    }
    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getUsername() {
        return attribute.get("name").toString();
    }

    public String getEmail(){
        return attribute.get("email").toString();
    }
}
