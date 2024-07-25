package com.harpsharp.auth.service;

import com.harpsharp.auth.dto.*;
import com.harpsharp.auth.dto.response.GoogleResponse;
import com.harpsharp.auth.dto.response.KaKaoResponse;
import com.harpsharp.auth.dto.response.NaverResponse;
import com.harpsharp.auth.entity.UserEntity;
import com.harpsharp.auth.oauth2.*;
import com.harpsharp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

// 유저 정보를 획득
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2Response oAuth2Response = null;

        String registrationId = userRequest.getClientRegistration().getRegistrationId();


        if(registrationId.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        if(registrationId.equals("google")){
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        if(registrationId.equals("kakao")){
            oAuth2Response = new KaKaoResponse(oAuth2User.getAttributes());
        }

        if(oAuth2Response == null) return null;

        String email = oAuth2Response.getEmail();

        UserEntity duplicated = userRepository
                .findByUsername(email)
                .orElseThrow(NullPointerException::new);

        if(duplicated == null){
            UserEntity userEntity = UserEntity.builder()
                    .username(oAuth2Response.getUsername())
                    .email(email)
                    .role("ROLE_USER")
                    .social_type(registrationId)
                    .build();

            userRepository.save(userEntity);

            UserDTO userDTO = UserDTO.builder()
                    .username(oAuth2Response.getUsername())
                    .email(email)
                    .role("ROLE_USER")
                    .social_type(registrationId)
                    .build();

            return new CustomOAuth2User(userDTO);
        }



        return null;
    }
}
