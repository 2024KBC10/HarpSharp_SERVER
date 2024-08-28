package com.harpsharp.auth.config;

import com.harpsharp.auth.jwt.*;
import com.harpsharp.auth.service.CustomOAuth2UserService;
import com.harpsharp.auth.service.UserService;
import com.harpsharp.infra_rds.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ResponseUtils responseUtils;
    private final UserService userService;


    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofNone();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers("/error");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http
                //.cors(cors -> cors.configurationSource(request -> corsConfiguration))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/api/v1/", "/api/v1/join", "/api/v1/user/board/**", "/api/v1/user/todo/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/**")
                        .permitAll()
                        .requestMatchers("/admin")
                        .hasRole("ADMIN")
                        .requestMatchers("/reissue")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .exceptionHandling((exceptions) -> exceptions
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(new JwtFilter(jwtUtil, responseUtils), LoginFilter.class)
                .addFilterBefore(new JwtExceptionHandlerFilter(responseUtils), JwtFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, userService, responseUtils), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, responseUtils), LogoutFilter.class)
                .addFilterBefore(new LogoutExceptionHandlerFilter(responseUtils), CustomLogoutFilter.class)
                .sessionManagement((session)->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }


}