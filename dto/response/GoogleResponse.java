package com.harpsharp.auth.dto.response;

import com.harpsharp.auth.oauth2.OAuth2Response;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getUsername() {
        return attribute.get("name").toString();
    }

    public String getEmail(){
        return attribute.get("email").toString();
    }
}
