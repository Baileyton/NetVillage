package com.example.demo.service;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    public void save(Member member){
        Member findByNick = memberRepository.findByNick(member.getNick());
        if(findByNick != null){
            throw new IllegalStateException("이미 사용중인 닉네임입니다.");
        }
        memberRepository.save(member);
    }

    public Member login(String nick, String rawPassword) {
        Member member = memberRepository.findByNick((nick));
        if(member == null){
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        if(!passwordEncoder.matches(rawPassword,member.getPassword())){
            throw new IllegalArgumentException("비밀번호를 다시 입력해주세요");
        }

        return member;
    }
}
