package com.harpsharp.infra_rds.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeleteDTO {
    private String password;
}
