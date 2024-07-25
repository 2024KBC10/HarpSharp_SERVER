package com.harpsharp.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@ResponseBody
@RequiredArgsConstructor
@Tag(name = "관리자 페이지", description = "ROLE_ADMIN 테스트 페이지")
public class AdminController {
    @GetMapping("/admin")
    public String adminAPI(){
        return "admin page";
    }
}
