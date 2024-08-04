package com.harpsharp.infra_rds.dto.user;


public record UserDTO(String username,
                      String email,
                      String role,
                      String social_type) {
}
