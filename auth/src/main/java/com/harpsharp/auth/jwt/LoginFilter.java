package com.harpsharp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.dto.CustomUserDetails;
import com.harpsharp.auth.dto.LoginDTO;
import com.harpsharp.auth.dto.response.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // JSON 문자열 읽기
            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            System.out.println("messageBody = " + messageBody);
            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            LoginDTO loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);
            System.out.println("loginDTO = " + loginDTO);

            // 사용자 이름과 비밀번호 추출
            String username = loginDTO.getUsername();
            String password = loginDTO.getPassword();
            System.out.println("username = " + username);
            System.out.println("password = " + password);

            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

            // 인증 시도
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            System.out.println("e = " + e);
            throw new AuthenticationServiceException("Failed to parse authentication request body", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Long   userId   = customUserDetails.getUserId();
        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String accessToken   = jwtUtil.createAccessToken(userId, username, role);
        String refreshToken  = jwtUtil.createRefreshToken(userId, username, role);

        jwtUtil.addRefreshEntity(accessToken, refreshToken);


        ApiResponse responseDTO = ApiResponse.builder()
                .code("USER_LOGGED_IN")
                .message(username + " logged in successfully")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(responseDTO);


        response.setHeader("access", accessToken);
        response.addCookie(jwtUtil.createCookie("refresh", refreshToken));
        response.setContentType("application/json");
        response.setStatus(HttpStatus.CREATED.value());
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
    }
}