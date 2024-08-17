package com.harpsharp.auth.jwt;

import com.harpsharp.auth.service.UserService;
import com.harpsharp.auth.utils.CustomUserDetails;
import com.harpsharp.infra_rds.dto.user.ResponseUserDTO;
import com.harpsharp.infra_rds.util.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final ResponseUtils responseUtils;

    private String username;
    private String password;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        parseRequest(request);
        // 인증 토큰 생성
        System.out.println("username + \" \"+password = " + username + " "+password);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        // 인증 시도
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        Map<Long, ResponseUserDTO> user = userService.findByUsername(username);
        jwtUtil.responseLogin(response, username, role, user);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        responseUtils.writeResponseEntity(response, HttpStatus.UNAUTHORIZED.value(), "INVALID_INPUT", "유효하지 않은 입력입니다.");
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        parseRequest(request);
        return username;
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        parseRequest(request);
        return password;
    }

    private void parseRequest(HttpServletRequest request) {
        try {
            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            Map<String, String> loginData = responseUtils.readValue(messageBody, Map.class);

            this.username = loginData.get("username");
            this.password = loginData.get("password");

        } catch (IOException e) {
            log.error("Error parsing login request", e);
            throw new AuthenticationServiceException("Invalid login data format", e);
        }
    }
}
