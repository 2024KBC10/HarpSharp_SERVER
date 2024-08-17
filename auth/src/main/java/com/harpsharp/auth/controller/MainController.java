package com.harpsharp.auth.controller;

import com.harpsharp.infra_rds.dto.user.InfoDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

@RestController
public class MainController {
    @GetMapping("/api/v1/")
    public ResponseEntity<ApiResponse> mainAPI(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        InfoDTO infoDTO = new InfoDTO(username, role);

        ApiResponse apiResponse = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "ROOT_PAGE",
                "Welcome to Auth!");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }
}
