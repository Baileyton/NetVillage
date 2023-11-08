package com.example.demo.entity;

import com.example.demo.dto.MemberDto;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nick", unique = true)
    private String nick;

    private String password;

    public static Member join(MemberDto memberDto, String encodedPassword){
        Member member = new Member();
        member.setNick(memberDto.getNick());
        member.setPassword(encodedPassword);
        return member;
    }
}
