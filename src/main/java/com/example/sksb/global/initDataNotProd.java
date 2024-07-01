package com.example.sksb.global;

import com.example.sksb.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class initDataNotProd {
    private final MemberService memberService;

    @Bean
    ApplicationRunner initNotProd() {
        return args -> {
            memberService.join("admin","1234");
            memberService.join("user1","1234");
            memberService.join("user2","1234");
        };
    }
}
