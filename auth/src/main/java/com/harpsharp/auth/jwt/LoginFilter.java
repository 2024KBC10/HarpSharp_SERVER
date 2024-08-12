package com.harpsharp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.service.UserService;
import com.harpsharp.auth.utils.CustomUserDetails;
import com.harpsharp.infra_rds.dto.response.ResponseWithData;
import com.harpsharp.infra_rds.dto.user.LoginDTO;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.auth.entity.RefreshToken;
import com.harpsharp.auth.service.RefreshTokenService;
import com.harpsharp.infra_rds.dto.user.ResponseUserDTO;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.infra_rds.mapper.UserMapper;
import com.harpsharp.infra_rds.repository.UserRepository;
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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final UserService userService;


    private String username;
    private String password;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            String username = obtainUsername(request);
            String password = obtainPassword(request);
//
            System.out.println("username = " + username);
            System.out.println("password = " + password);

            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

            // 인증 시도
            return authenticationManager.authenticate(authToken);


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

        RefreshToken refreshEntity = RefreshToken
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        refreshTokenService.save(refreshEntity);

        Map<Long, ResponseUserDTO> user = userService.findByUsername(username);

        ResponseWithData<Map<Long, ResponseUserDTO>> responseDTO = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "TOKEN_PUBLISHED_SUCCESSFULLY",
                username + "님이 로그인 했습니다.",
                user);


        String json = objectMapper.writeValueAsString(responseDTO);


        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(jwtUtil.createCookie("refresh", refreshToken));
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.CREATED.value());
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");


        ApiResponse responseDTO = new ApiResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "INVALID_AUTHENTICATION",
                "유효하지 않은 접근입니다.");
        response.getWriter().write(objectMapper.writeValueAsString(responseDTO));
        response.getWriter().flush();
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        if (username == null) {
            parseRequest(request);
        }
        return username;
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        if (password == null) {
            parseRequest(request);
        }
        return password;
    }

    private void parseRequest(HttpServletRequest request) {
        try {
            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            Map<String, String> loginData = objectMapper.readValue(messageBody, Map.class);

            this.username = loginData.get("username");
            this.password = loginData.get("password");

        } catch (IOException e) {
            log.error("Error parsing login request", e);
            throw new AuthenticationServiceException("Invalid login data format", e);
        }
    }
}
