package com.example.demo.controller;

import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final PasswordEncoder passwordEncoder;

    private final MemberService memberService;

    //회원가입 화면
    @GetMapping(value = "/join")
    public String joinForm(MemberDto memberDto, Model model){
        model.addAttribute("memberDto", new MemberDto());
        return "member/joinForm";
    }

    //회원가입
    @PostMapping(value = "/join")
    public String join(@Valid MemberDto memberDto, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            //유효성 검사
            return "member/joinForm";
        }

        try{
            String encodedPassword = passwordEncoder.encode(memberDto.getPassword());
            Member member = Member.join(memberDto, encodedPassword);
            memberService.save(member);
            log.info("join info: " + member);
        } catch (IllegalAccessError e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/joinForm";
        }

        return "home";
    }
}
