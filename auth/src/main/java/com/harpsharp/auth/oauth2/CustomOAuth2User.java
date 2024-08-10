package com.harpsharp.auth.oauth2;

import com.harpsharp.infra_rds.dto.user.RequestUserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final RequestUserDTO requestUserDTO;

    public CustomOAuth2User(RequestUserDTO requestUserDTO) {
        this.requestUserDTO = requestUserDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return requestUserDTO.role();
            }
        });

        return authorities;
    }

    @Override
    public String getName() {
        return requestUserDTO.username();
    }
}
