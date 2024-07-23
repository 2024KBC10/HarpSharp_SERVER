package com.harpsharp.auth.controller;

import com.harpsharp.auth.dto.JoinDTO;
import com.harpsharp.auth.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
@Tag(name = "회원가입 API", description = "회원가입 관련 API")
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    @Operation(summary = "회원가입(USER)", description = "일반 계정 가입을 위한 API")
    @ApiResponse(responseCode = "201", description = "", content = @Content(schema = @Schema(implementation = JoinDTO.class)))
    public ResponseEntity<JoinDTO> joinUser(JoinDTO joinDTO){
        joinService.registerUser(joinDTO, "ROLE_USER");
        return ResponseEntity.status(HttpStatus.CREATED).body(joinDTO);
    }

    @PostMapping("/admin/join")
    @Operation(summary = "회원가입(ADMIN)", description = "관리자 계정 가입을 위한 API")
    @ApiResponse(responseCode = "201", description = "", content = @Content(schema = @Schema(implementation = JoinDTO.class)))
    public ResponseEntity<JoinDTO> joinAdmin(JoinDTO joinDTO){
        joinService.registerUser(joinDTO, "ROLE_ADMIN");
        return ResponseEntity.status(HttpStatus.CREATED).body(joinDTO);
    }
}
