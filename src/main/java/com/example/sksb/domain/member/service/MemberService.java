package com.example.sksb.domain.member.service;

import com.example.sksb.domain.member.entity.Member;
import com.example.sksb.domain.member.repository.MemberRepository;
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

}
