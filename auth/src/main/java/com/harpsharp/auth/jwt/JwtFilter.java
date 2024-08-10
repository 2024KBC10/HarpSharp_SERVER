package com.harpsharp.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.utils.CustomUserDetails;
import com.harpsharp.infra_rds.dto.response.ApiResponse;
import com.harpsharp.infra_rds.entity.User;
import com.harpsharp.auth.exceptions.JwtAuthenticationException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("Authorization");

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.substring("Bearer ".length());

        try{
            jwtUtil.isExpired(accessToken);
        }catch(JwtException e){
            ApiResponse apiResponse =
                    new ApiResponse(
                            LocalDateTime.now(),
                            HttpStatus.UNAUTHORIZED.value(),
                            "INVALID_ACCESS",
                            "유효하지 않은 접근입니다."
                    );
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json;charset=UTF-8");
            String json = objectMapper.writeValueAsString(apiResponse);
            response.getWriter().write(json);
            response.getWriter().flush();
            return;
        }


        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);


        if (!category.equals("Authorization")) {

            throw JwtAuthenticationException
                    .builder()
                    .code("IS_NOT_ACCESS_TOKEN")
                    .message("유효하지 않은 토큰입니다.")
                    .build();
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        User user = User.builder()
                .username(username)
                .role(role)
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
