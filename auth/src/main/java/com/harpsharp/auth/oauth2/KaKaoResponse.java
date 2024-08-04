package com.harpsharp.auth.oauth2;

import java.util.Map;

public class KaKaoResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    public KaKaoResponse(Map<String, Object> attribute){
        this.attribute = (Map<String, Object>) attribute.get("response");
    }
    @Override
    public String getProvider() {
        return "kakao";
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
