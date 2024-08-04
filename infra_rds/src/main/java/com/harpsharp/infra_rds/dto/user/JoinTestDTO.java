package com.harpsharp.infra_rds.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinTestDTO {
    private String username;
    private String password;
    private String email;
    //private String profile_image;
}