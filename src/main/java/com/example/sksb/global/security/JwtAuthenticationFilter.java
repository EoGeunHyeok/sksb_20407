package com.example.sksb.global.security;

import com.example.sksb.domain.member.service.MemberService;
import com.example.sksb.global.rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Rq rq;
    private final MemberService memberService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String accessToken = rq.getCookieValue("accessToken", "");

        if (!accessToken.isBlank()) {
            String refreshToken = rq.getCookieValue("refreshToken", "");

            SecurityUser securityUser = memberService.getUserFromAccessToken(accessToken);
            rq.setLogin(securityUser);

        }

        filterChain.doFilter(request, response);

    }
}