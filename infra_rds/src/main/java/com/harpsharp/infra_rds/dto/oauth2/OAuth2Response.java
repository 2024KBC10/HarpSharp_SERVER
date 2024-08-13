package com.harpsharp.infra_rds.dto.oauth2;

public interface OAuth2Response {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getUsername();
}
