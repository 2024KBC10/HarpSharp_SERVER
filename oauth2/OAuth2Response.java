package com.harpsharp.auth.oauth2;

public interface OAuth2Response {
    String getProvider();
    String getProviderId();
    String getUsername();
    String getEmail();
}
