package com.harpsharp.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class AdminController {
    @GetMapping("/admin")
    public String adminAPI(){
        return "admin page";
    }
}
