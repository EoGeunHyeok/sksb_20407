package com.example.sksb.domain.member.controller;

import com.example.sksb.domain.member.dto.MemberDto;
import com.example.sksb.domain.member.entity.Member;
import com.example.sksb.domain.member.service.MemberService;
import com.example.sksb.global.rq.Rq;
import com.example.sksb.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @AllArgsConstructor
    @Getter
    public static class LoginResponseBody {
       private MemberDto item;
    }

    @Getter
    @Setter
    public static class LoginRequestBody {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @PostMapping("/login")
    public RsData<LoginResponseBody> login(
            @Valid @RequestBody LoginRequestBody body
    ) {
        RsData<MemberService.AuthAndMakeTokensResponseBody> authAndMakeTokensRs = memberService.authAndMakeTokens(body.getUsername(), body.getPassword());

        // Rq에 쿠키 설정해놓은걸 만들었기때문에
        // 토큰값을 만들어 설정해 주는 부분
        // XSS 공격 방지용으로 사용
        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().getRefreshToken());
        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().getAccessToken());

        return RsData.of(
                authAndMakeTokensRs.getResultCode(),
                authAndMakeTokensRs.getMsg(),
                new LoginResponseBody(
                     new MemberDto(
                             authAndMakeTokensRs.getData().getMember()
                     )
                )
        );
    }
    @Getter
    public static class MeResponseBody {
        private MemberDto item;

        public MeResponseBody(Member member) {
            this.item = new MemberDto(member);
        }
    }
    @GetMapping(value = "/me")
    public RsData<MeResponseBody> getMe(){
        return RsData.of(
                "200",
                "내 정보 가져오기 성공!",
                new MeResponseBody(rq.getMember())
        );
    }
}