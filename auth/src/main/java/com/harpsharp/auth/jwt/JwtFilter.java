package com.harpsharp.auth.jwt;

import com.harpsharp.auth.utils.CustomUserDetails;
import com.harpsharp.auth.exceptions.JwtAuthenticationException;
import com.harpsharp.infra_rds.util.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ResponseUtils responseUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("Authorization");
        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if(!accessToken.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = accessToken.substring("Bearer ".length());

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if(cookies == null){
            filterChain.doFilter(request, response);
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refreshToken = cookie.getValue();
            }
        }

        if(refreshToken == null){
            filterChain.doFilter(request, response);
            return;
        }


        if (!jwtUtil.getCategory(accessToken).equals("Authorization")) {

            throw JwtAuthenticationException
                    .builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("INVALID_ACCESS_TOKEN")
                    .build();
        }

        if (!jwtUtil.getCategory(refreshToken).equals("refresh"))
            throw JwtAuthenticationException
                    .builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("INVALID_ACCESS_TOKEN")
                    .build();

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        if(jwtUtil.isExpired(refreshToken)){
            filterChain.doFilter(request, response);
            return;
        }

        if(jwtUtil.isExpired(accessToken)) {
            responseUtils.writeResponseEntity(response, jwtUtil.reissueToken(username, role, accessToken, refreshToken));
        }

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
