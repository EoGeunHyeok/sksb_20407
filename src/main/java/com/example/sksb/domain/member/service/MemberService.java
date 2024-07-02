package com.example.sksb.domain.member.service;

import com.example.sksb.domain.member.entity.Member;
import com.example.sksb.domain.member.repository.MemberRepository;
import com.example.sksb.global.rsData.RsData;
import com.example.sksb.global.excepctions.GlobalException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private  final AuthTokenService authTokenService;

    public Optional<Member> findByUsername(String username){
        return memberRepository.findByUsername(username);
    }

    public boolean passwordMatches(Member member, String password) {
        return passwordEncoder.matches(member.getPassword(), password);
    }

    @Transactional
    public void join(String username, String password) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .build();
       memberRepository.save(member);
    }

    @Getter
    public static class AuthAndMakeTokensResponseBody {
        private String accessToken;
        private String refreshToken;

        public AuthAndMakeTokensResponseBody(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Transactional
    public RsData<AuthAndMakeTokensResponseBody> authAndMakeTokens(String username, String password) {
        Member member = findByUsername(username)
                .orElseThrow(() -> new GlobalException("400-1", "해당 유저가 존재하지 않습니다."));

        if (!passwordMatches(member, password))
            throw new GlobalException("400-2", "비밀번호가 일치하지 않습니다.");

        String refreshToken = authTokenService.genRefreshToken(member); // 진짜 토큰은 아니고 임의적으로 만들어서 한것.
        String accessToken = authTokenService.genAccessToken(member); // 진짜 토큰은 아니고 임의적으로 만들어서 한것.

        return RsData.of(
                "200-1",
                "로그인 성공",
                new AuthAndMakeTokensResponseBody(accessToken, refreshToken)
        );

    }

}
