package com.harpsharp.infra_rds.dto.oauth2;

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
