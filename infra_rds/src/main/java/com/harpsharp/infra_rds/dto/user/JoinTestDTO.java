package com.harpsharp.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class JoinTestDTO {
    private String username;
    private String password;
    private String email;
    //private String profile_image;
}