package com.harpsharp.auth.controller;

import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.auth.utils.RedirectURI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SwaggerController {
    @RequestMapping(value = "/docs/auth", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse> docsAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(RedirectURI.DOCS_AUTH.getUri()));
        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }
}
