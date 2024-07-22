package com.harpsharp.auth.controller;

import com.harpsharp.auth.dto.JoinDTO;
import com.harpsharp.auth.dto.response.ErrorResponse;
import com.harpsharp.auth.exceptions.UserAlreadyExistsException;
import com.harpsharp.auth.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<JoinDTO> joinUser(JoinDTO joinDTO){
        joinService.registerUser(joinDTO, "ROLE_ADMIN");
        return ResponseEntity.ok(joinDTO);
    }

    @PostMapping("/admin/join")
    public ResponseEntity<JoinDTO> joinAdmin(JoinDTO joinDTO){
        joinService.registerUser(joinDTO, "ROLE_ADMIN");
        return ResponseEntity.ok(joinDTO);
    }
}
