package com.harpsharp.auth.controller;

import com.harpsharp.auth.dto.InfoDTO;
import com.harpsharp.auth.dto.JoinDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Iterator;

@Controller
@Tag(name = "기본 페이지", description = "root 페이지")
public class MainController {
    @GetMapping("/")
    @ResponseBody
    @Operation(summary = "JWT 유저 정보 테스트", description = "JWT에 담긴 닉네임 및 권한을 추출")
    @ApiResponse(responseCode = "201", description = "", content = @Content(schema = @Schema(implementation = InfoDTO.class)))
    public ResponseEntity<?> mainAPI(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        InfoDTO infoDTO = InfoDTO
                .builder()
                .username(username)
                .role(role)
                .build();

        return ResponseEntity.status(201).body(infoDTO);
    }
}
