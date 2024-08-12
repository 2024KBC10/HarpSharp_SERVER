package com.harpsharp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // JSON 문자열 읽기
            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            // JSON 파싱
            LoginDTO loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);

            // 사용자 이름과 비밀번호 추출
            String username = loginDTO.username();
            String password = loginDTO.password();


            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

            // 인증 시도
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            System.out.println("e = " + e);

            throw new AuthenticationServiceException("인증에 실패했습니다.", e);
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

        RefreshToken refreshEntity = RefreshToken
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        refreshTokenService.save(refreshEntity);

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_USERNAME"));

        ResponseWithData<Map<Long, ResponseUserDTO>> responseDTO = new ResponseWithData<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "TOKEN_PUBLISHED_SUCCESSFULLY",
                username + "님이 로그인 했습니다.",
                userMapper.toMap(user));


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
}
