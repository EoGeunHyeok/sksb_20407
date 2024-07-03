package com.example.sksb.domain.member.service;

import com.example.sksb.domain.member.entity.Member;
import com.example.sksb.domain.member.repository.MemberRepository;
import com.example.sksb.global.excepctions.GlobalException;
import com.example.sksb.global.rsData.RsData;
import com.example.sksb.global.security.SecurityUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public boolean passwordMatches(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }

    @Transactional
    public void join(String username, String password) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        String refreshToken = authTokenService.genRefreshToken(member);
        member.setRefreshToken(refreshToken);

        memberRepository.save(member);
    }



    @Getter
    @AllArgsConstructor
    public static class AuthAndMakeTokensResponseBody {
        private Member member;
        private String accessToken;
        private String refreshToken;
    }

    @Transactional
    public RsData<AuthAndMakeTokensResponseBody> authAndMakeTokens(String username, String password) {
        Member member = findByUsername(username)
                .orElseThrow(() -> new GlobalException("400-1", "해당 유저가 존재하지 않습니다."));

        if (!passwordMatches(member, password))
            throw new GlobalException("400-2", "비밀번호가 일치하지 않습니다.");

        String refreshToken = member.getRefreshToken();
        String accessToken = authTokenService.genAccessToken(member);

        return RsData.of(
                "200-1",
                "로그인 성공",
                new AuthAndMakeTokensResponseBody(member, accessToken, refreshToken)
        );
    }

    public SecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = authTokenService.getDataFrom(accessToken);

        long id = (Integer) payloadBody.get("id");
        String username = (String) payloadBody.get("username");
        List<String> authorities = (List<String>) payloadBody.get("authorities");

        return new SecurityUser(
                id,
                username,
                "",

                authorities.stream().map(SimpleGrantedAuthority::new).toList()
                // 사용자 정보를 SimpleGrantedAuthority 이러한 객체로 변환후 리스트로 보냄
        );
    }

    public RsData<String> refreshAccessToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new GlobalException("400-1", "존재하지 않는 리프레시 토큰입니다."));

        String accessToken = authTokenService.genAccessToken(member);

        return RsData.of("200-1", "토큰 갱신 성공", accessToken);

    }

    public boolean validateToken(String accessToken) {
        return authTokenService.validateToken(accessToken);
    }

}
