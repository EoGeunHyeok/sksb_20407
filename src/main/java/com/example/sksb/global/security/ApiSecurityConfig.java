package com.example.sksb.global.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // method에서 보안을 추가할 수 있는것.
@RequiredArgsConstructor
public class ApiSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/api/**")
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/api/*/members/login","/api/*/members/logout")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )

                .csrf(
                        csrf -> csrf
                                .disable()
                )
                .cors( //도메인에서 요청을 하거나 제한하는데 사용
                        cors -> cors.configure(http)
                )
                .sessionManagement( // 매니지먼트 세션관리 비활성화
                        sessionManagement -> sessionManagement
                                .disable()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}