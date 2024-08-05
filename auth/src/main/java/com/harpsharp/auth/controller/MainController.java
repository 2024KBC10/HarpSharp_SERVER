package com.harpsharp.auth.controller;

import com.harpsharp.infra_rds.dto.user.InfoDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.auth.utils.BaseResponse;
import org.springframework.http.HttpStatus;
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
public class MainController {
    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<ApiResponse> mainAPI(){
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


        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .code("ROOT_PAGE")
                        .message("Welcome to Harpsharp!")
                        .build());
    }
}
