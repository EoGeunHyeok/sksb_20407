package com.example.sksb.global.rq;

import com.example.sksb.domain.member.service.MemberService;
import com.example.sksb.global.security.SecurityUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
// http 요청을 계속 받을때마다 새로운 객체를 생성해서 받음
@RequestScope
// http 요청을 계속 받을때마다 새로운 객체를 생성해서 받음
@RequiredArgsConstructor
public class Rq {
    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    // 일반
    public boolean isAjax() {
        // 해당 요청이 아작스 인지 확인하는 부분
        if ("application/json".equals(req.getHeader("Accept"))) return true;
        return "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
    }

    // 쿠키 관련

    public void setCookie(String name, String value) {
        // 쿠키 설정
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    public void setCrossDomainCookie(String name, String value) {
        // cross도메인에서 사용가능하게 설정
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();

        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public Cookie getCookie(String name) {
        Cookie[] cookies = req.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }

        return null;
    }

    public String getCookieValue(String name, String defaultValue) {
        Cookie cookie = getCookie(name);

        if (cookie == null) {
            return defaultValue;
        }

        return cookie.getValue();
    }

    private long getCookieAsLong(String name, int defaultValue) {
        String value = getCookieValue(name, null);

        if (value == null) {
            return defaultValue;
        }

        return Long.parseLong(value);
    }

    public void removeCookie(String name) {
        // 저거후 패스 무효화
        Cookie cookie = getCookie(name);

        if (cookie == null) {
            return;
        }

        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);

        ResponseCookie responseCookie = ResponseCookie.from(name, null)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .build();

        resp.addHeader("Set-Cookie", responseCookie.toString());
    }
    public void setLogin(SecurityUser securityUser){
        SecurityContextHolder.getContext().setAuthentication(securityUser.genAuthentication());
    }
}