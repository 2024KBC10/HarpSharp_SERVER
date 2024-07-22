package com.harpsharp.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {
    private String username;
    private String password;
    private String email;
    //private String profile_image;
}
